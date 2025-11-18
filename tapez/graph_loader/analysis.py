"""Code analysis and metrics calculation"""

from dataclasses import dataclass, field
from typing import Dict, List, Set
from pathlib import Path
from .cfg_builder import CFGBuilder, ControlFlowGraph
from ..parser.core_parser import HLASMParser
from ..parser.models import ParseResult


@dataclass
class AnalysisResult:
    """Result of code analysis."""

    file_path: str
    control_flow_graph: ControlFlowGraph
    parse_result: ParseResult
    cyclomatic_complexity: int = 0
    complexities_by_label: Dict[str, int] = field(default_factory=dict)
    dependency_map: Dict[str, Set[str]] = field(default_factory=dict)
    flattened_instructions: List = field(default_factory=list)

    def to_dict(self) -> dict:
        """Convert analysis result to dictionary for JSON export."""
        return {
            "file_path": self.file_path,
            "cyclomatic_complexity": self.cyclomatic_complexity,
            "total_instructions": len(self.parse_result.instructions),
            "total_labels": len(self.parse_result.labels),
            "total_blocks": len(self.control_flow_graph.blocks),
            "complexities_by_label": self.complexities_by_label,
            "dependencies": {k: list(v) for k, v in self.dependency_map.items()},
            "errors": self.parse_result.errors,
            "warnings": self.parse_result.warnings,
        }


class CodeAnalyzer:
    """Analyzes HLASM code and produces metrics and graphs."""

    def __init__(self):
        self.parser = HLASMParser()
        self.cfg_builder = CFGBuilder()

    def analyze_file(self, file_path: Path) -> AnalysisResult:
        """
        Analyze a HLASM file and return comprehensive results.

        Args:
            file_path: Path to the HLASM file

        Returns:
            AnalysisResult containing CFG, metrics, and other analysis data
        """
        # Parse the file
        parse_result = self.parser.parse_file(file_path)

        # Build control flow graph
        cfg = self.cfg_builder.build(parse_result)

        # Calculate overall cyclomatic complexity
        cyclomatic_complexity = cfg.calculate_cyclomatic_complexity()

        # Calculate complexity by label/section
        complexities_by_label = self._calculate_complexities_by_label(cfg)

        # Identify dependencies (external calls)
        dependency_map = self._identify_dependencies(parse_result)

        # Create flattened instruction list
        flattened = [inst for inst in parse_result.instructions]

        result = AnalysisResult(
            file_path=str(file_path),
            control_flow_graph=cfg,
            parse_result=parse_result,
            cyclomatic_complexity=cyclomatic_complexity,
            complexities_by_label=complexities_by_label,
            dependency_map=dependency_map,
            flattened_instructions=flattened,
        )

        return result

    def _calculate_complexities_by_label(
        self, cfg: ControlFlowGraph
    ) -> Dict[str, int]:
        """Calculate cyclomatic complexity for each labeled section."""
        complexities = {}

        for label, block_id in cfg.label_to_block.items():
            # For simplicity, we'll calculate complexity for the entire section
            # In a more sophisticated implementation, we'd create sub-graphs
            # for each section and calculate their individual complexities
            block = cfg.get_block(block_id)
            if block:
                # Count branches in this block's instructions
                branch_count = sum(
                    1
                    for inst in block.instructions
                    if self.cfg_builder._is_branch_instruction(inst)
                )
                # Simplified complexity: 1 + number of branches
                complexities[label] = 1 + branch_count

        return complexities

    def _identify_dependencies(self, parse_result: ParseResult) -> Dict[str, Set[str]]:
        """
        Identify external program dependencies.

        Looks for CALL, LINK, XCTL and similar instructions.
        """
        dependencies = {}
        call_instructions = {"CALL", "LINK", "XCTL", "LOAD", "BALR", "BASR"}

        for instruction in parse_result.instructions:
            if instruction.mnemonic.upper() in call_instructions:
                # Extract the called program name
                if instruction.operands:
                    target = instruction.operands[0].value.strip("'\"")
                    if "main" not in dependencies:
                        dependencies["main"] = set()
                    dependencies["main"].add(target)

        return dependencies
