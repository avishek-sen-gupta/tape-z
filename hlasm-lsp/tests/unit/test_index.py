from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex


SAMPLE_SOURCE = """\
MYPROG   CSECT
R14      EQU   14
R15      EQU   15
         USING *,R15
         LR    R14,R15
LOOP     BCT   R14,LOOP
         END
"""


def _build_index(source: str = SAMPLE_SOURCE) -> DocumentIndex:
    parser = HlasmParser()
    tree = parser.parse(source)
    return DocumentIndex.from_tree(tree, "file:///test.hlasm")


def test_definitions_found():
    index = _build_index()
    assert "MYPROG" in index.definitions
    assert "R14" in index.definitions
    assert "R15" in index.definitions
    assert "LOOP" in index.definitions


def test_definition_location():
    index = _build_index()
    locs = index.definitions["MYPROG"]
    assert len(locs) == 1
    assert locs[0].line == 0
    assert locs[0].character == 0


def test_references_found():
    index = _build_index()
    assert "R14" in index.references
    assert "R15" in index.references
    assert "LOOP" in index.references


def test_reference_count():
    index = _build_index()
    assert len(index.references["R14"]) >= 2


def test_sections_found():
    index = _build_index()
    assert len(index.sections) == 1
    assert index.sections[0].name == "MYPROG"
    assert index.sections[0].kind == "CSECT"


def test_equ_values():
    index = _build_index()
    assert "R14" in index.equ_values
    assert index.equ_values["R14"] == "14"


def test_get_symbol_at_position():
    index = _build_index()
    sym = index.get_symbol_at(4, 15)
    assert sym is not None
    assert sym.name == "R14"


def test_macro_blocks_empty_when_no_macros():
    index = _build_index()
    assert len(index.macro_blocks) == 0


def test_macro_blocks_found():
    source = """\
         MACRO
         MYMAC
         LR    1,2
         MEND
"""
    index = _build_index(source)
    assert len(index.macro_blocks) == 1
    assert index.macro_blocks[0].start_line == 0
    assert index.macro_blocks[0].end_line == 3
