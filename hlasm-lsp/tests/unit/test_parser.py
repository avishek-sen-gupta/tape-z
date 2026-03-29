from hlasm_lsp.parser import HlasmParser


SAMPLE_SOURCE = """\
* Comment line
MYPROG   CSECT
R14      EQU   14
         USING *,R15
         LR    R12,R15
         END
"""


def test_parse_returns_tree():
    parser = HlasmParser()
    tree = parser.parse(SAMPLE_SOURCE)
    assert tree is not None
    assert tree.root_node.type == "source_file"


def test_parse_finds_instruction_statements():
    parser = HlasmParser()
    tree = parser.parse(SAMPLE_SOURCE)
    root = tree.root_node
    instruction_nodes = [
        child for child in root.children if child.type == "instruction_statement"
    ]
    assert len(instruction_nodes) == 5


def test_parse_finds_comment_lines():
    parser = HlasmParser()
    tree = parser.parse(SAMPLE_SOURCE)
    root = tree.root_node
    comment_nodes = [child for child in root.children if child.type == "comment_line"]
    assert len(comment_nodes) == 1


def test_parse_no_errors_on_valid_source():
    parser = HlasmParser()
    tree = parser.parse(SAMPLE_SOURCE)
    errors = parser.get_error_nodes(tree)
    assert len(errors) == 0


def test_parse_detects_errors():
    parser = HlasmParser()
    tree = parser.parse("         LR    R1,\n")
    errors = parser.get_error_nodes(tree)
    assert len(errors) > 0
