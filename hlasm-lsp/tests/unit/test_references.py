from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.references import find_references

SAMPLE = """\
MYPROG   CSECT
COUNTER  EQU   5
         LA    R1,COUNTER
         ST    R1,COUNTER
"""
URI = "file:///test.hlasm"


def _build_index() -> DocumentIndex:
    parser = HlasmParser()
    tree = parser.parse(SAMPLE)
    return DocumentIndex.from_tree(tree, URI)


def test_references_include_definition_and_usages():
    index = _build_index()
    refs = find_references(index, 2, 18, include_definition=True)
    assert len(refs) >= 3


def test_references_without_definition():
    index = _build_index()
    refs = find_references(index, 2, 18, include_definition=False)
    assert len(refs) >= 2


def test_references_empty_for_unknown():
    index = _build_index()
    refs = find_references(index, 0, 50, include_definition=True)
    assert len(refs) == 0
