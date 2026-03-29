import ctypes
import logging
import os
import subprocess
import tempfile
from pathlib import Path

from tree_sitter import Language, Parser, Tree, Node

logger = logging.getLogger(__name__)


def _find_grammar_dir() -> Path:
    env_path = os.environ.get("HLASM_GRAMMAR_PATH")
    if env_path:
        return Path(env_path)
    package_root = Path(__file__).resolve().parent.parent
    return package_root.parent / "tree-sitter-hlasm" / "src"


def _build_shared_library(grammar_dir: Path) -> str:
    parser_c = grammar_dir / "parser.c"
    if not parser_c.exists():
        raise FileNotFoundError(
            f"parser.c not found at {parser_c}. "
            f"Run 'npx tree-sitter generate' in the tree-sitter-hlasm directory."
        )

    so_path = os.path.join(tempfile.gettempdir(), "tree-sitter-hlasm.so")
    result = subprocess.run(
        [
            "cc",
            "-shared",
            "-fPIC",
            "-O2",
            "-I",
            str(grammar_dir),
            str(parser_c),
            "-o",
            so_path,
        ],
        capture_output=True,
        text=True,
    )
    if result.returncode != 0:
        raise RuntimeError(f"Failed to compile tree-sitter grammar: {result.stderr}")
    logger.info("Compiled tree-sitter-hlasm to %s", so_path)
    return so_path


def _load_language() -> Language:
    grammar_dir = _find_grammar_dir()
    so_path = _build_shared_library(grammar_dir)
    lib = ctypes.cdll.LoadLibrary(so_path)
    lib.tree_sitter_hlasm.restype = ctypes.c_void_p
    language_ptr = lib.tree_sitter_hlasm()
    return Language(language_ptr)


class HlasmParser:
    def __init__(self) -> None:
        self._language = _load_language()
        self._parser = Parser(self._language)

    def parse(self, source: str) -> Tree:
        return self._parser.parse(source.encode("utf-8"))

    def parse_incremental(self, source: str, old_tree: Tree) -> Tree:
        return self._parser.parse(source.encode("utf-8"), old_tree)

    def get_error_nodes(self, tree: Tree) -> list[Node]:
        errors: list[Node] = []
        self._collect_errors(tree.root_node, errors)
        return errors

    def _collect_errors(self, node: Node, errors: list[Node]) -> None:
        if node.type == "ERROR" or node.is_missing:
            errors.append(node)
        for child in node.children:
            self._collect_errors(child, errors)
