from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.definition import find_definition

SAMPLE = """\
MYPROG   CSECT
COUNTER  EQU   5
         LA    R1,COUNTER
"""
URI = "file:///test.hlasm"


def _build_index() -> DocumentIndex:
    parser = HlasmParser()
    tree = parser.parse(SAMPLE)
    return DocumentIndex.from_tree(tree, URI)


def test_definition_found():
    index = _build_index()
    result = find_definition(index, 2, 18)
    assert result is not None
    assert result.line == 1
    assert result.character == 0


def test_definition_not_found_for_undefined():
    index = _build_index()
    result = find_definition(index, 2, 15)
    assert result is None
