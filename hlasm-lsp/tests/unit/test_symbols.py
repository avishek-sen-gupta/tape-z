from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.symbols import get_document_symbols

SAMPLE = """\
MYPROG   CSECT
R14      EQU   14
LOOP     LR    R14,R15
         END
"""
URI = "file:///test.hlasm"


def _build_index() -> DocumentIndex:
    parser = HlasmParser()
    tree = parser.parse(SAMPLE)
    return DocumentIndex.from_tree(tree, URI)


def test_document_symbols_returns_sections():
    index = _build_index()
    syms = get_document_symbols(index)
    section_names = [s.name for s in syms]
    assert "MYPROG" in section_names


def test_section_has_children():
    index = _build_index()
    syms = get_document_symbols(index)
    myprog = [s for s in syms if s.name == "MYPROG"][0]
    child_names = [c.name for c in myprog.children]
    assert "R14" in child_names
    assert "LOOP" in child_names
