from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.semantic_tokens import collect_semantic_tokens, TOKEN_TYPES


def test_token_types_defined():
    assert "keyword" in TOKEN_TYPES
    assert "function" in TOKEN_TYPES
    assert "variable" in TOKEN_TYPES
    assert "comment" in TOKEN_TYPES
    assert "number" in TOKEN_TYPES
    assert "string" in TOKEN_TYPES
    assert "type" in TOKEN_TYPES
    assert "label" in TOKEN_TYPES
    assert "parameter" in TOKEN_TYPES
    assert "operator" in TOKEN_TYPES


def test_comment_gets_comment_token():
    parser = HlasmParser()
    tree = parser.parse("* This is a comment\n")
    tokens = collect_semantic_tokens(tree)
    assert any(t.token_type == "comment" for t in tokens)


def test_label_gets_label_token():
    parser = HlasmParser()
    tree = parser.parse("MYLABEL  CSECT\n")
    tokens = collect_semantic_tokens(tree)
    label_tokens = [t for t in tokens if t.token_type == "label"]
    assert len(label_tokens) >= 1
    assert label_tokens[0].text == "MYLABEL"


def test_operation_gets_keyword_or_function_token():
    parser = HlasmParser()
    tree = parser.parse("         LR    R1,R2\n")
    tokens = collect_semantic_tokens(tree)
    op_tokens = [t for t in tokens if t.text == "LR"]
    assert len(op_tokens) == 1
    assert op_tokens[0].token_type in ("keyword", "function")


def test_number_gets_number_token():
    parser = HlasmParser()
    tree = parser.parse("R14      EQU   14\n")
    tokens = collect_semantic_tokens(tree)
    num_tokens = [t for t in tokens if t.token_type == "number"]
    assert any(t.text == "14" for t in num_tokens)


def test_dc_type_gets_type_token():
    parser = HlasmParser()
    tree = parser.parse("FWORD    DC    F'200'\n")
    tokens = collect_semantic_tokens(tree)
    type_tokens = [t for t in tokens if t.token_type == "type"]
    assert len(type_tokens) >= 1
