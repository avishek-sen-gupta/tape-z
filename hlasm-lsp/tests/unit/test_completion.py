from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.completion import get_completions

SAMPLE = """\
MYPROG   CSECT
COUNTER  EQU   5
         LR    R1,R2
"""
URI = "file:///test.hlasm"


def _build_index() -> DocumentIndex:
    parser = HlasmParser()
    tree = parser.parse(SAMPLE)
    return DocumentIndex.from_tree(tree, URI)


def test_completions_include_mnemonics():
    index = _build_index()
    items = get_completions(index)
    labels = [item.label for item in items]
    assert "LR" in labels
    assert "MVC" in labels
    assert "CSECT" in labels


def test_completions_include_defined_labels():
    index = _build_index()
    items = get_completions(index)
    labels = [item.label for item in items]
    assert "MYPROG" in labels
    assert "COUNTER" in labels


def test_completions_include_registers():
    index = _build_index()
    items = get_completions(index)
    labels = [item.label for item in items]
    assert "R0" in labels
    assert "R15" in labels
