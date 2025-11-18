"""Export Control Flow Graphs and analysis results to Neo4J"""

from typing import Optional
from .connection import Neo4JConnection
from ..graph_loader.cfg_builder import ControlFlowGraph, BasicBlock
from ..graph_loader.analysis import AnalysisResult


class Neo4JExporter:
    """Exports HLASM analysis results to Neo4J."""

    def __init__(self, connection: Optional[Neo4JConnection] = None):
        self.connection = connection or Neo4JConnection()

    def export_cfg(self, cfg: ControlFlowGraph, clear_existing: bool = False):
        """
        Export a Control Flow Graph to Neo4J.

        Args:
            cfg: Control Flow Graph to export
            clear_existing: Whether to clear existing data first
        """
        if clear_existing:
            self.connection.clear_database()

        # Create nodes for each basic block
        for block_id, block in cfg.blocks.items():
            self._create_block_node(block, is_entry=(block_id == cfg.entry_block))

        # Create relationships
        for from_node, to_node, data in cfg.graph.edges(data=True):
            edge_type = data.get("edge_type", "FLOWS_TO")
            self._create_relationship(from_node, to_node, edge_type)

    def export_analysis_result(self, result: AnalysisResult, clear_existing: bool = False):
        """
        Export complete analysis result to Neo4J.

        Args:
            result: Analysis result to export
            clear_existing: Whether to clear existing data first
        """
        self.export_cfg(result.control_flow_graph, clear_existing)

        # Add metadata node
        self._create_metadata_node(result)

    def _create_block_node(self, block: BasicBlock, is_entry: bool = False):
        """Create a node for a basic block."""
        properties = {
            "block_id": block.id,
            "label": block.label or "",
            "start_line": block.start_line or 0,
            "end_line": block.end_line or 0,
            "instruction_count": len(block.instructions),
            "is_entry": is_entry,
        }

        # Add instruction details
        instructions = []
        for inst in block.instructions:
            inst_str = f"{inst.mnemonic}"
            if inst.operands:
                inst_str += " " + ",".join(op.value for op in inst.operands)
            instructions.append(inst_str)
        properties["instructions"] = "; ".join(instructions)

        query = """
        CREATE (b:BasicBlock {
            block_id: $block_id,
            label: $label,
            start_line: $start_line,
            end_line: $end_line,
            instruction_count: $instruction_count,
            is_entry: $is_entry,
            instructions: $instructions
        })
        """
        self.connection.execute_query(query, properties)

    def _create_relationship(self, from_block: str, to_block: str, edge_type: str):
        """Create a relationship between two blocks."""
        query = f"""
        MATCH (from:BasicBlock {{block_id: $from_block}})
        MATCH (to:BasicBlock {{block_id: $to_block}})
        CREATE (from)-[:{edge_type}]->(to)
        """
        self.connection.execute_query(
            query, {"from_block": from_block, "to_block": to_block}
        )

    def _create_metadata_node(self, result: AnalysisResult):
        """Create a metadata node with analysis results."""
        properties = {
            "file_path": result.file_path,
            "cyclomatic_complexity": result.cyclomatic_complexity,
            "total_instructions": len(result.parse_result.instructions),
            "total_labels": len(result.parse_result.labels),
            "total_blocks": len(result.control_flow_graph.blocks),
        }

        query = """
        CREATE (m:Metadata {
            file_path: $file_path,
            cyclomatic_complexity: $cyclomatic_complexity,
            total_instructions: $total_instructions,
            total_labels: $total_labels,
            total_blocks: $total_blocks
        })
        """
        self.connection.execute_query(query, properties)

    def export_flowchart(self, cfg: ControlFlowGraph, clear_existing: bool = False):
        """
        Export flowchart representation to Neo4J.

        Similar to CFG but with additional visualization properties.
        """
        # For flowcharts, we can add additional properties for rendering
        self.export_cfg(cfg, clear_existing)

        # Add flowchart-specific labels
        query = """
        MATCH (b:BasicBlock)
        SET b:FlowchartNode
        """
        self.connection.execute_query(query)
