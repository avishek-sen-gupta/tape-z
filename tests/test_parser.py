"""Tests for HLASM parser"""

import pytest
from pathlib import Path
from tapez.parser.core_parser import HLASMParser
from tapez.parser.models import InstructionType


def test_parser_initialization():
    """Test that parser can be initialized."""
    parser = HLASMParser()
    assert parser is not None


def test_parse_comment_line():
    """Test parsing a comment line."""
    parser = HLASMParser()
    instruction = parser._parse_line("* This is a comment", 1)

    assert instruction is not None
    assert instruction.instruction_type == InstructionType.COMMENT
    assert instruction.comment == "This is a comment"


def test_parse_simple_instruction():
    """Test parsing a simple instruction."""
    parser = HLASMParser()
    instruction = parser._parse_line("         LR    R1,R2", 1)

    assert instruction is not None
    assert instruction.mnemonic == "LR"
    assert len(instruction.operands) == 2
    assert instruction.operands[0].value == "R1"
    assert instruction.operands[1].value == "R2"


def test_parse_instruction_with_label():
    """Test parsing an instruction with a label."""
    parser = HLASMParser()
    instruction = parser._parse_line("START    L     R1,=F'10'", 1)

    assert instruction is not None
    assert instruction.label is not None
    assert instruction.label.name == "START"
    assert instruction.mnemonic == "L"


def test_empty_line():
    """Test parsing an empty line."""
    parser = HLASMParser()
    instruction = parser._parse_line("", 1)

    assert instruction is None


def test_parse_result_success():
    """Test ParseResult success property."""
    from tapez.parser.models import ParseResult

    result = ParseResult()
    assert result.success is True

    result.add_error("Test error")
    assert result.success is False
