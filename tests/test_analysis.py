"""Tests for code analysis"""

import pytest
from tapez.graph_loader.analysis import CodeAnalyzer, AnalysisResult


def test_analyzer_initialization():
    """Test that analyzer can be initialized."""
    analyzer = CodeAnalyzer()
    assert analyzer is not None


def test_analysis_result_to_dict():
    """Test converting analysis result to dictionary."""
    from tapez.graph_loader.cfg_builder import ControlFlowGraph
    from tapez.parser.models import ParseResult

    result = AnalysisResult(
        file_path="test.hlasm",
        control_flow_graph=ControlFlowGraph(),
        parse_result=ParseResult(),
        cyclomatic_complexity=5,
    )

    data = result.to_dict()

    assert isinstance(data, dict)
    assert data["file_path"] == "test.hlasm"
    assert data["cyclomatic_complexity"] == 5
    assert "total_instructions" in data
    assert "total_labels" in data
