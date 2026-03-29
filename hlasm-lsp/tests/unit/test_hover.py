from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.hover import get_hover_info

SAMPLE = """\
MYPROG   CSECT
R14      EQU   14
         LR    R14,R15
         DC    F'200'
"""
URI = "file:///test.hlasm"


def _build_index() -> DocumentIndex:
    parser = HlasmParser()
    tree = parser.parse(SAMPLE)
    return DocumentIndex.from_tree(tree, URI)


def test_hover_on_equ_symbol():
    index = _build_index()
    result = get_hover_info(index, 2, 15)
    assert result is not None
    assert "EQU" in result
    assert "14" in result


def test_hover_on_unknown_symbol():
    index = _build_index()
    result = get_hover_info(index, 2, 50)
    assert result is None
