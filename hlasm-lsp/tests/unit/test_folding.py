from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.folding import get_folding_ranges

URI = "file:///test.hlasm"


def _build_index(source: str) -> DocumentIndex:
    parser = HlasmParser()
    tree = parser.parse(source)
    return DocumentIndex.from_tree(tree, URI)


def test_macro_block_folding():
    source = """\
         MACRO
         MYMAC
         LR    1,2
         MEND
         END
"""
    index = _build_index(source)
    ranges = get_folding_ranges(index)
    macro_ranges = [r for r in ranges if r.kind == "region"]
    assert len(macro_ranges) == 1
    assert macro_ranges[0].start_line == 0
    assert macro_ranges[0].end_line == 3


def test_section_folding():
    source = """\
SECT1    CSECT
         LR    1,2
SECT2    CSECT
         LR    3,4
         END
"""
    index = _build_index(source)
    ranges = get_folding_ranges(index)
    section_ranges = [r for r in ranges if r.kind == "region"]
    assert len(section_ranges) >= 2


def test_comment_block_folding():
    source = """\
* Comment 1
* Comment 2
* Comment 3
         END
"""
    index = _build_index(source)
    ranges = get_folding_ranges(index)
    comment_ranges = [r for r in ranges if r.kind == "comment"]
    assert len(comment_ranges) == 1
    assert comment_ranges[0].start_line == 0
    assert comment_ranges[0].end_line == 2
