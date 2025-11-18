"""Control Flow Graph builder for HLASM code"""

import networkx as nx
from typing import Dict, List, Set, Optional
from dataclasses import dataclass, field
from ..parser.models import Instruction, ParseResult
from ..format_loader.mnemonics_loader import MnemonicsLoader
from ..common.id_provider import UUIDProvider


@dataclass
class BasicBlock:
    """Represents a basic block in the control flow graph."""

    id: str
    instructions: List[Instruction] = field(default_factory=list)
    label: Optional[str] = None
    start_line: Optional[int] = None
    end_line: Optional[int] = None

    def add_instruction(self, instruction: Instruction):
        """Add an instruction to this basic block."""
        self.instructions.append(instruction)
        if self.start_line is None:
            self.start_line = instruction.line_number
        self.end_line = instruction.line_number

    def __str__(self) -> str:
        return f"BasicBlock({self.id}, lines {self.start_line}-{self.end_line})"


@dataclass
class ControlFlowGraph:
    """Control Flow Graph representation."""

    graph: nx.DiGraph = field(default_factory=nx.DiGraph)
    blocks: Dict[str, BasicBlock] = field(default_factory=dict)
    entry_block: Optional[str] = None
    exit_blocks: Set[str] = field(default_factory=set)
    label_to_block: Dict[str, str] = field(default_factory=dict)

    def add_block(self, block: BasicBlock):
        """Add a basic block to the CFG."""
        self.blocks[block.id] = block
        self.graph.add_node(block.id, block=block)
        if block.label:
            self.label_to_block[block.label] = block.id

    def add_edge(self, from_block: str, to_block: str, edge_type: str = "FLOW"):
        """Add an edge between two blocks."""
        self.graph.add_edge(from_block, to_block, edge_type=edge_type)

    def get_block(self, block_id: str) -> Optional[BasicBlock]:
        """Get a basic block by ID."""
        return self.blocks.get(block_id)

    def get_block_by_label(self, label: str) -> Optional[BasicBlock]:
        """Get a basic block by its label."""
        block_id = self.label_to_block.get(label)
        return self.blocks.get(block_id) if block_id else None

    def calculate_cyclomatic_complexity(self) -> int:
        """
        Calculate cyclomatic complexity: M = E - N + 2P
        where E = edges, N = nodes, P = connected components
        """
        E = self.graph.number_of_edges()
        N = self.graph.number_of_nodes()
        P = nx.number_weakly_connected_components(self.graph)
        return E - N + 2 * P if N > 0 else 0


class CFGBuilder:
    """Builds Control Flow Graphs from parsed HLASM code."""

    def __init__(self):
        self.mnemonics_loader = MnemonicsLoader()
        self.id_provider = UUIDProvider()

    def build(self, parse_result: ParseResult) -> ControlFlowGraph:
        """Build a CFG from parsed instructions."""
        cfg = ControlFlowGraph()

        if not parse_result.instructions:
            return cfg

        # First pass: create basic blocks
        current_block = self._create_new_block()
        cfg.entry_block = current_block.id

        for instruction in parse_result.instructions:
            # Start new block if instruction has a label
            if instruction.label and current_block.instructions:
                cfg.add_block(current_block)
                current_block = self._create_new_block(label=instruction.label.name)

            current_block.add_instruction(instruction)

            # End current block and start new one if instruction is a branch
            if self._is_branch_instruction(instruction):
                cfg.add_block(current_block)
                current_block = self._create_new_block()

        # Add the last block if it has instructions
        if current_block.instructions:
            cfg.add_block(current_block)
            cfg.exit_blocks.add(current_block.id)

        # Second pass: connect blocks with edges
        self._connect_blocks(cfg, parse_result)

        return cfg

    def _create_new_block(self, label: Optional[str] = None) -> BasicBlock:
        """Create a new basic block with a unique ID."""
        block_id = self.id_provider.generate_with_prefix("block")
        return BasicBlock(id=block_id, label=label)

    def _is_branch_instruction(self, instruction: Instruction) -> bool:
        """Check if an instruction is a branch instruction."""
        return self.mnemonics_loader.is_branch_instruction(instruction.mnemonic)

    def _connect_blocks(self, cfg: ControlFlowGraph, parse_result: ParseResult):
        """Connect basic blocks with appropriate edges."""
        block_list = list(cfg.blocks.values())

        for i, block in enumerate(block_list):
            if not block.instructions:
                continue

            last_instruction = block.instructions[-1]

            # If it's a branch instruction, add edge to target
            if self._is_branch_instruction(last_instruction):
                target_label = self._extract_branch_target(last_instruction)
                if target_label:
                    target_block = cfg.get_block_by_label(target_label)
                    if target_block:
                        cfg.add_edge(block.id, target_block.id, "BRANCH")

                # For conditional branches, also add fall-through edge
                if self._is_conditional_branch(last_instruction):
                    if i + 1 < len(block_list):
                        cfg.add_edge(block.id, block_list[i + 1].id, "FALL_THROUGH")
            else:
                # Add sequential flow to next block
                if i + 1 < len(block_list):
                    cfg.add_edge(block.id, block_list[i + 1].id, "SEQUENTIAL")
                else:
                    # This is an exit block
                    cfg.exit_blocks.add(block.id)

    def _extract_branch_target(self, instruction: Instruction) -> Optional[str]:
        """Extract the target label from a branch instruction."""
        if instruction.operands:
            # The target is typically the first operand for most branch instructions
            return instruction.operands[0].value
        return None

    def _is_conditional_branch(self, instruction: Instruction) -> bool:
        """Check if a branch instruction is conditional."""
        # BC, BCR are conditional, B, BR are unconditional
        conditional_branches = {"BC", "BCR", "BZ", "BNZ", "BE", "BNE", "BH", "BL"}
        return instruction.mnemonic.upper() in conditional_branches
