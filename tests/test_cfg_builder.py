"""Tests for CFG builder"""

import pytest
from tapez.graph_loader.cfg_builder import CFGBuilder, BasicBlock
from tapez.parser.models import Instruction, ParseResult, InstructionType


def test_cfg_builder_initialization():
    """Test that CFG builder can be initialized."""
    builder = CFGBuilder()
    assert builder is not None


def test_basic_block_creation():
    """Test basic block creation."""
    block = BasicBlock(id="block_1", label="START")
    assert block.id == "block_1"
    assert block.label == "START"
    assert len(block.instructions) == 0


def test_basic_block_add_instruction():
    """Test adding instruction to basic block."""
    block = BasicBlock(id="block_1")
    instruction = Instruction(mnemonic="L", line_number=10)

    block.add_instruction(instruction)

    assert len(block.instructions) == 1
    assert block.start_line == 10
    assert block.end_line == 10


def test_build_empty_cfg():
    """Test building CFG from empty parse result."""
    builder = CFGBuilder()
    result = ParseResult()

    cfg = builder.build(result)

    assert cfg is not None
    assert len(cfg.blocks) == 0


def test_build_simple_cfg():
    """Test building CFG from simple instructions."""
    builder = CFGBuilder()
    result = ParseResult()

    # Add some simple instructions
    result.add_instruction(Instruction(mnemonic="L", line_number=1))
    result.add_instruction(Instruction(mnemonic="A", line_number=2))
    result.add_instruction(Instruction(mnemonic="ST", line_number=3))

    cfg = builder.build(result)

    assert cfg is not None
    assert len(cfg.blocks) > 0
    assert cfg.entry_block is not None


def test_cyclomatic_complexity_empty():
    """Test cyclomatic complexity calculation for empty graph."""
    from tapez.graph_loader.cfg_builder import ControlFlowGraph

    cfg = ControlFlowGraph()
    complexity = cfg.calculate_cyclomatic_complexity()

    assert complexity == 0
