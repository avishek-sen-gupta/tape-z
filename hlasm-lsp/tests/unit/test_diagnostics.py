from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.diagnostics import extract_diagnostics


def test_no_diagnostics_on_valid_source():
    parser = HlasmParser()
    tree = parser.parse("MYPROG   CSECT\n         END\n")
    diagnostics = extract_diagnostics(tree)
    assert len(diagnostics) == 0


def test_diagnostics_on_parse_error():
    parser = HlasmParser()
    tree = parser.parse("         LR    R1,\n")
    diagnostics = extract_diagnostics(tree)
    assert len(diagnostics) > 0
    diag = diagnostics[0]
    assert diag.range.start.line == 0
    assert diag.severity == 1  # Error
    assert len(diag.message) > 0
