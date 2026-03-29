from __future__ import annotations

from dataclasses import dataclass
from tree_sitter import Tree, Node

from hlasm_lsp.mnemonics import ASSEMBLER_DIRECTIVES

TOKEN_TYPES: list[str] = [
    "comment",
    "keyword",
    "function",
    "variable",
    "parameter",
    "label",
    "number",
    "string",
    "type",
    "operator",
]

TOKEN_MODIFIERS: list[str] = [
    "declaration",
    "definition",
]


@dataclass(frozen=True)
class SemanticToken:
    line: int
    character: int
    length: int
    token_type: str
    text: str


def collect_semantic_tokens(tree: Tree) -> list[SemanticToken]:
    tokens: list[SemanticToken] = []
    _walk(tree.root_node, tokens)
    tokens.sort(key=lambda t: (t.line, t.character))
    return tokens


def _text(node: Node) -> str:
    return node.text.decode("utf-8")


def _add(tokens: list[SemanticToken], node: Node, token_type: str) -> None:
    text = _text(node)
    lines = text.split("\n")
    tokens.append(
        SemanticToken(
            line=node.start_point[0],
            character=node.start_point[1],
            length=len(lines[0]),
            token_type=token_type,
            text=text,
        )
    )


def _walk(node: Node, tokens: list[SemanticToken]) -> None:
    if node.type == "comment_line" or node.type == "macro_comment_line":
        _add(tokens, node, "comment")
        return

    if node.type == "instruction_statement":
        _process_instruction(node, tokens)
        return

    if node.type == "process_statement":
        _add(tokens, node, "keyword")
        return

    for child in node.children:
        _walk(child, tokens)


def _process_instruction(node: Node, tokens: list[SemanticToken]) -> None:
    label_node = node.child_by_field_name("label")
    operation_node = node.child_by_field_name("operation")
    operands_node = node.child_by_field_name("operands")

    if label_node is not None:
        _add(tokens, label_node, "label")

    if operation_node is not None:
        op_text = _text(operation_node).upper()
        if op_text in ASSEMBLER_DIRECTIVES:
            _add(tokens, operation_node, "keyword")
        else:
            _add(tokens, operation_node, "function")

    if operands_node is not None:
        _walk_operands(operands_node, tokens)


def _walk_operands(node: Node, tokens: list[SemanticToken]) -> None:
    if node.type == "symbol":
        _add(tokens, node, "variable")
        return
    if node.type == "variable_symbol":
        _add(tokens, node, "parameter")
        return
    if node.type == "number":
        _add(tokens, node, "number")
        return
    if node.type == "string_literal":
        _add(tokens, node, "string")
        return
    if node.type == "dc_type_spec":
        _add(tokens, node, "type")
        return
    if node.type == "dc_value":
        _add(tokens, node, "string")
        return
    if node.type == "location_counter":
        _add(tokens, node, "number")
        return
    if node.type in ("hex_self_defining_term", "binary_self_defining_term"):
        _add(tokens, node, "number")
        return
    if node.type in ("character_self_defining_term", "graphic_self_defining_term"):
        _add(tokens, node, "string")
        return
    for child in node.children:
        _walk_operands(child, tokens)
