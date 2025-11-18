"""HLASM instruction format definitions and builders"""

from dataclasses import dataclass, field
from typing import List, Dict, Optional
from enum import Enum


class OperandType(Enum):
    """Type of operand in HLASM instruction."""

    REGISTER = "REGISTER"
    ADDRESS = "ADDRESS"
    IMMEDIATE = "IMMEDIATE"
    LABEL = "LABEL"
    EXPRESSION = "EXPRESSION"


@dataclass
class OperandFormat:
    """Format specification for an operand."""

    position: int
    operand_type: OperandType
    optional: bool = False
    description: str = ""


@dataclass
class HLASMInstructionFormat:
    """Format definition for a HLASM instruction."""

    mnemonic: str
    format_type: str  # e.g., "RR", "RX", "RS", "SI", etc.
    operands: List[OperandFormat] = field(default_factory=list)
    description: str = ""
    opcode: Optional[str] = None

    def validate_operands(self, operand_values: List[str]) -> bool:
        """Validate that the provided operands match this format."""
        required_count = sum(1 for op in self.operands if not op.optional)
        total_count = len(self.operands)

        operand_count = len(operand_values)
        return required_count <= operand_count <= total_count


class InstructionFormatBuilder:
    """Builder for creating instruction format definitions."""

    def __init__(self):
        self.formats: Dict[str, HLASMInstructionFormat] = {}
        self._initialize_common_formats()

    def _initialize_common_formats(self):
        """Initialize common HLASM instruction formats."""
        # RR format (Register-to-Register)
        self.add_format(
            HLASMInstructionFormat(
                mnemonic="AR",
                format_type="RR",
                operands=[
                    OperandFormat(0, OperandType.REGISTER, description="Target register"),
                    OperandFormat(1, OperandType.REGISTER, description="Source register"),
                ],
                description="Add Register",
            )
        )

        # RX format (Register and Indexed Storage)
        self.add_format(
            HLASMInstructionFormat(
                mnemonic="L",
                format_type="RX",
                operands=[
                    OperandFormat(0, OperandType.REGISTER, description="Target register"),
                    OperandFormat(1, OperandType.ADDRESS, description="Storage address"),
                ],
                description="Load",
            )
        )

        # RS format (Register and Storage)
        self.add_format(
            HLASMInstructionFormat(
                mnemonic="STM",
                format_type="RS",
                operands=[
                    OperandFormat(0, OperandType.REGISTER, description="First register"),
                    OperandFormat(1, OperandType.REGISTER, description="Last register"),
                    OperandFormat(2, OperandType.ADDRESS, description="Storage address"),
                ],
                description="Store Multiple",
            )
        )

        # SI format (Storage and Immediate)
        self.add_format(
            HLASMInstructionFormat(
                mnemonic="MVI",
                format_type="SI",
                operands=[
                    OperandFormat(0, OperandType.ADDRESS, description="Storage address"),
                    OperandFormat(1, OperandType.IMMEDIATE, description="Immediate value"),
                ],
                description="Move Immediate",
            )
        )

        # SS format (Storage-to-Storage)
        self.add_format(
            HLASMInstructionFormat(
                mnemonic="MVC",
                format_type="SS",
                operands=[
                    OperandFormat(0, OperandType.ADDRESS, description="Target address"),
                    OperandFormat(1, OperandType.ADDRESS, description="Source address"),
                ],
                description="Move Character",
            )
        )

        # Branch instructions
        self.add_format(
            HLASMInstructionFormat(
                mnemonic="B",
                format_type="RX",
                operands=[
                    OperandFormat(0, OperandType.LABEL, description="Branch target"),
                ],
                description="Branch Unconditional",
            )
        )

    def add_format(self, format_def: HLASMInstructionFormat):
        """Add an instruction format definition."""
        self.formats[format_def.mnemonic] = format_def

    def get_format(self, mnemonic: str) -> Optional[HLASMInstructionFormat]:
        """Get the format definition for a mnemonic."""
        return self.formats.get(mnemonic.upper())

    def has_format(self, mnemonic: str) -> bool:
        """Check if a format exists for the mnemonic."""
        return mnemonic.upper() in self.formats
