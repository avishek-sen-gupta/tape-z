"""Flowchart generation for HLASM code"""

import graphviz
from pathlib import Path
from typing import Optional
from .cfg_builder import ControlFlowGraph, BasicBlock


class FlowchartBuilder:
    """Builds flowcharts from Control Flow Graphs."""

    def __init__(self):
        pass

    def build_flowchart(
        self, cfg: ControlFlowGraph, output_path: Path, format: str = "svg"
    ) -> str:
        """
        Build a flowchart from a CFG and save to file.

        Args:
            cfg: Control Flow Graph
            output_path: Output file path (without extension)
            format: Output format (svg, png, pdf, etc.)

        Returns:
            Path to the generated flowchart file
        """
        dot = graphviz.Digraph(comment="HLASM Flowchart")
        dot.attr(rankdir="TB")  # Top to bottom layout
        dot.attr("node", shape="box", style="rounded")

        # Add nodes
        for block_id, block in cfg.blocks.items():
            label = self._format_block_label(block)
            node_shape = "ellipse" if block_id == cfg.entry_block else "box"

            if block_id in cfg.exit_blocks:
                dot.node(block_id, label, shape="ellipse", style="filled", fillcolor="lightgray")
            else:
                dot.node(block_id, label, shape=node_shape)

        # Add edges
        for from_node, to_node, data in cfg.graph.edges(data=True):
            edge_type = data.get("edge_type", "FLOW")
            edge_label = self._format_edge_label(edge_type)
            edge_style = "dashed" if edge_type == "FALL_THROUGH" else "solid"
            dot.edge(from_node, to_node, label=edge_label, style=edge_style)

        # Render the flowchart
        output_file = str(output_path).replace(f".{format}", "")
        dot.render(output_file, format=format, cleanup=True)

        return f"{output_file}.{format}"

    def _format_block_label(self, block: BasicBlock) -> str:
        """Format a basic block for display in the flowchart."""
        lines = []

        # Add label if present
        if block.label:
            lines.append(f"[{block.label}]")

        # Add instructions (limited to first few)
        max_instructions = 5
        for i, instruction in enumerate(block.instructions[:max_instructions]):
            if instruction.label:
                lines.append(f"{instruction.label.name}:")
            lines.append(f"  {instruction.mnemonic} {self._format_operands(instruction)}")

        if len(block.instructions) > max_instructions:
            lines.append(f"  ... ({len(block.instructions) - max_instructions} more)")

        return "\\n".join(lines)

    def _format_operands(self, instruction) -> str:
        """Format operands for display."""
        if not instruction.operands:
            return ""
        return ",".join(op.value for op in instruction.operands)

    def _format_edge_label(self, edge_type: str) -> str:
        """Format edge label for display."""
        labels = {
            "BRANCH": "branch",
            "FALL_THROUGH": "fall-through",
            "SEQUENTIAL": "",
            "FLOW": "",
        }
        return labels.get(edge_type, edge_type)

    def build_section_flowcharts(
        self, cfg: ControlFlowGraph, output_dir: Path, format: str = "svg"
    ) -> dict:
        """
        Build separate flowcharts for each labeled section.

        Args:
            cfg: Control Flow Graph
            output_dir: Output directory for flowchart files
            format: Output format

        Returns:
            Dictionary mapping section names to output file paths
        """
        output_dir.mkdir(parents=True, exist_ok=True)
        results = {}

        for label, block_id in cfg.label_to_block.items():
            section_cfg = self._extract_section_cfg(cfg, block_id)
            output_path = output_dir / f"flowchart_{label}"
            flowchart_file = self.build_flowchart(section_cfg, output_path, format)
            results[label] = flowchart_file

        return results

    def _extract_section_cfg(self, cfg: ControlFlowGraph, start_block_id: str) -> ControlFlowGraph:
        """Extract a sub-CFG for a specific section."""
        # For simplicity, we'll just create a CFG with the single block
        # In a more sophisticated implementation, we'd traverse the graph
        # to include all reachable blocks until the next section
        section_cfg = ControlFlowGraph()
        block = cfg.get_block(start_block_id)
        if block:
            section_cfg.add_block(block)
            section_cfg.entry_block = block.id
            section_cfg.exit_blocks.add(block.id)

        return section_cfg
