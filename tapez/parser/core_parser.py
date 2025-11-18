"""Core HLASM parser implementation"""

import re
from pathlib import Path
from typing import List, Optional
from .models import Instruction, Label, Operand, ParseResult, InstructionType
from ..common.file_utils import FileReader


class HLASMParser:
    """
    Parser for HLASM (High Level Assembler) code.

    This is a simplified parser that handles basic HLASM syntax.
    For production use, this should be replaced with ANTLR4-generated parser.
    """

    # Common HLASM mnemonics
    EXECUTABLE_MNEMONICS = {
        "L", "LA", "LR", "ST", "STM", "LM", "MVC", "MVI", "CLI", "CLC",
        "B", "BR", "BC", "BCR", "BAS", "BASR", "BAL", "BALR",
        "A", "S", "M", "D", "AR", "SR", "MR", "DR",
        "C", "CR", "CH", "N", "O", "X", "NR", "OR", "XR",
        "SLA", "SRA", "SLL", "SRL", "SLDL", "SRDL",
        "CVB", "CVD", "PACK", "UNPK",
        "EX", "IC", "STC", "TM", "TR", "TRT",
    }

    DIRECTIVE_MNEMONICS = {
        "CSECT", "DSECT", "START", "END", "ENTRY", "EXTRN",
        "DC", "DS", "EQU", "ORG", "LTORG",
        "USING", "DROP", "COPY", "MACRO", "MEND",
        "TITLE", "EJECT", "SPACE", "PRINT",
    }

    def __init__(self):
        self.file_reader = FileReader()

    def parse_file(self, file_path: Path) -> ParseResult:
        """Parse a HLASM file and return the result."""
        result = ParseResult()
        result.metadata["file_path"] = str(file_path)

        try:
            lines = self.file_reader.read_hlasm_file(file_path)
            for line_num, line in enumerate(lines, start=1):
                try:
                    instruction = self._parse_line(line, line_num)
                    if instruction:
                        result.add_instruction(instruction)
                except Exception as e:
                    result.add_error(f"Line {line_num}: {str(e)}")
        except Exception as e:
            result.add_error(f"Failed to read file: {str(e)}")

        return result

    def _parse_line(self, line: str, line_number: int) -> Optional[Instruction]:
        """Parse a single HLASM line."""
        # Skip empty lines
        if not line.strip():
            return None

        # Check for comment line (asterisk in column 1)
        if line.startswith("*"):
            return Instruction(
                mnemonic="COMMENT",
                line_number=line_number,
                instruction_type=InstructionType.COMMENT,
                comment=line[1:].strip(),
                raw_line=line,
            )

        # Parse label (columns 1-8 typically)
        label = None
        label_part = line[:9].strip()
        if label_part and not label_part[0].isspace():
            label = Label(name=label_part, line_number=line_number, column=0)

        # Parse operation (columns 10-14 typically)
        operation_start = 9
        remaining = line[operation_start:].strip()
        if not remaining:
            return None

        parts = remaining.split(maxsplit=1)
        mnemonic = parts[0].upper()

        # Check for embedded SQL
        if mnemonic == "EXEC" and len(parts) > 1 and "SQL" in parts[1].upper():
            return self._parse_sql_statement(line, line_number, label)

        # Parse operands
        operands = []
        comment = None
        if len(parts) > 1:
            operand_part = parts[1]
            # Check for inline comment
            if " " in operand_part:
                operand_str, comment = operand_part.split(None, 1)
            else:
                operand_str = operand_part

            # Split operands by comma (simplified)
            operands = [
                Operand(value=op.strip()) for op in operand_str.split(",") if op.strip()
            ]

        # Determine instruction type
        instruction_type = self._determine_instruction_type(mnemonic)

        return Instruction(
            mnemonic=mnemonic,
            line_number=line_number,
            label=label,
            operands=operands,
            instruction_type=instruction_type,
            comment=comment,
            raw_line=line,
        )

    def _parse_sql_statement(
        self, line: str, line_number: int, label: Optional[Label]
    ) -> Instruction:
        """Parse embedded SQL statement."""
        return Instruction(
            mnemonic="EXEC SQL",
            line_number=line_number,
            label=label,
            instruction_type=InstructionType.SQL,
            raw_line=line,
        )

    def _determine_instruction_type(self, mnemonic: str) -> InstructionType:
        """Determine the type of instruction based on mnemonic."""
        if mnemonic in self.EXECUTABLE_MNEMONICS:
            return InstructionType.EXECUTABLE
        elif mnemonic in self.DIRECTIVE_MNEMONICS:
            return InstructionType.DIRECTIVE
        elif mnemonic == "MACRO":
            return InstructionType.MACRO
        else:
            # Default to executable for unknown mnemonics
            return InstructionType.EXECUTABLE
