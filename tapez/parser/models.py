"""Data models for HLASM parsing"""

from dataclasses import dataclass, field
from typing import List, Optional, Dict, Any
from enum import Enum


class InstructionType(Enum):
    """Type of HLASM instruction."""

    EXECUTABLE = "EXECUTABLE"
    DIRECTIVE = "DIRECTIVE"
    MACRO = "MACRO"
    COMMENT = "COMMENT"
    SQL = "SQL"


@dataclass
class Label:
    """Represents a label in HLASM code."""

    name: str
    line_number: int
    column: int = 0

    def __str__(self) -> str:
        return self.name


@dataclass
class Operand:
    """Represents an operand in HLASM instruction."""

    value: str
    operand_type: str = "GENERAL"
    is_register: bool = False
    is_address: bool = False
    is_immediate: bool = False

    def __str__(self) -> str:
        return self.value


@dataclass
class Instruction:
    """Represents a parsed HLASM instruction."""

    mnemonic: str
    line_number: int
    label: Optional[Label] = None
    operands: List[Operand] = field(default_factory=list)
    instruction_type: InstructionType = InstructionType.EXECUTABLE
    comment: Optional[str] = None
    raw_line: str = ""

    def __str__(self) -> str:
        label_str = f"{self.label} " if self.label else ""
        operands_str = ",".join(str(op) for op in self.operands)
        return f"{label_str}{self.mnemonic} {operands_str}"


@dataclass
class ParseResult:
    """Result of parsing HLASM code."""

    instructions: List[Instruction] = field(default_factory=list)
    labels: Dict[str, Label] = field(default_factory=dict)
    errors: List[str] = field(default_factory=list)
    warnings: List[str] = field(default_factory=list)
    metadata: Dict[str, Any] = field(default_factory=dict)

    @property
    def success(self) -> bool:
        """Check if parsing was successful (no errors)."""
        return len(self.errors) == 0

    def add_instruction(self, instruction: Instruction) -> None:
        """Add an instruction to the result."""
        self.instructions.append(instruction)
        if instruction.label:
            self.labels[instruction.label.name] = instruction.label

    def add_error(self, error: str) -> None:
        """Add a parsing error."""
        self.errors.append(error)

    def add_warning(self, warning: str) -> None:
        """Add a parsing warning."""
        self.warnings.append(warning)
