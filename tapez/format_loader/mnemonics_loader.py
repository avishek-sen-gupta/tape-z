"""Loader for HLASM mnemonics and instruction definitions"""

from pathlib import Path
from typing import Dict, List, Optional
import json


class MnemonicsLoader:
    """
    Loads HLASM mnemonics and their definitions from various sources.

    In the Java version, this would load from CSV files or other data sources.
    """

    def __init__(self):
        self.mnemonics: Dict[str, dict] = {}
        self._load_builtin_mnemonics()

    def _load_builtin_mnemonics(self):
        """Load built-in HLASM mnemonics."""
        # Common HLASM instructions with their properties
        builtin = {
            # Load/Store instructions
            "L": {"type": "RX", "description": "Load", "category": "LOAD_STORE"},
            "LA": {"type": "RX", "description": "Load Address", "category": "LOAD_STORE"},
            "LR": {"type": "RR", "description": "Load Register", "category": "LOAD_STORE"},
            "ST": {"type": "RX", "description": "Store", "category": "LOAD_STORE"},
            "STM": {"type": "RS", "description": "Store Multiple", "category": "LOAD_STORE"},
            "LM": {"type": "RS", "description": "Load Multiple", "category": "LOAD_STORE"},
            # Arithmetic instructions
            "A": {"type": "RX", "description": "Add", "category": "ARITHMETIC"},
            "AR": {"type": "RR", "description": "Add Register", "category": "ARITHMETIC"},
            "S": {"type": "RX", "description": "Subtract", "category": "ARITHMETIC"},
            "SR": {"type": "RR", "description": "Subtract Register", "category": "ARITHMETIC"},
            "M": {"type": "RX", "description": "Multiply", "category": "ARITHMETIC"},
            "MR": {"type": "RR", "description": "Multiply Register", "category": "ARITHMETIC"},
            "D": {"type": "RX", "description": "Divide", "category": "ARITHMETIC"},
            "DR": {"type": "RR", "description": "Divide Register", "category": "ARITHMETIC"},
            # Logical instructions
            "N": {"type": "RX", "description": "AND", "category": "LOGICAL"},
            "NR": {"type": "RR", "description": "AND Register", "category": "LOGICAL"},
            "O": {"type": "RX", "description": "OR", "category": "LOGICAL"},
            "OR": {"type": "RR", "description": "OR Register", "category": "LOGICAL"},
            "X": {"type": "RX", "description": "Exclusive OR", "category": "LOGICAL"},
            "XR": {"type": "RR", "description": "XOR Register", "category": "LOGICAL"},
            # Branch instructions
            "B": {"type": "RX", "description": "Branch Unconditional", "category": "BRANCH"},
            "BR": {"type": "RR", "description": "Branch Register", "category": "BRANCH"},
            "BC": {"type": "RX", "description": "Branch on Condition", "category": "BRANCH"},
            "BCR": {"type": "RR", "description": "Branch Condition Register", "category": "BRANCH"},
            "BAS": {"type": "RX", "description": "Branch and Save", "category": "BRANCH"},
            "BASR": {"type": "RR", "description": "Branch and Save Register", "category": "BRANCH"},
            # Compare instructions
            "C": {"type": "RX", "description": "Compare", "category": "COMPARE"},
            "CR": {"type": "RR", "description": "Compare Register", "category": "COMPARE"},
            "CLI": {"type": "SI", "description": "Compare Logical Immediate", "category": "COMPARE"},
            "CLC": {"type": "SS", "description": "Compare Logical Character", "category": "COMPARE"},
            # Move instructions
            "MVC": {"type": "SS", "description": "Move Character", "category": "MOVE"},
            "MVI": {"type": "SI", "description": "Move Immediate", "category": "MOVE"},
            # Directives
            "CSECT": {"type": "DIRECTIVE", "description": "Control Section", "category": "DIRECTIVE"},
            "DSECT": {"type": "DIRECTIVE", "description": "Dummy Section", "category": "DIRECTIVE"},
            "DC": {"type": "DIRECTIVE", "description": "Define Constant", "category": "DIRECTIVE"},
            "DS": {"type": "DIRECTIVE", "description": "Define Storage", "category": "DIRECTIVE"},
            "EQU": {"type": "DIRECTIVE", "description": "Equate", "category": "DIRECTIVE"},
            "USING": {"type": "DIRECTIVE", "description": "Use Base Register", "category": "DIRECTIVE"},
            "DROP": {"type": "DIRECTIVE", "description": "Drop Base Register", "category": "DIRECTIVE"},
            "END": {"type": "DIRECTIVE", "description": "End of Assembly", "category": "DIRECTIVE"},
        }
        self.mnemonics.update(builtin)

    def load_from_file(self, file_path: Path) -> None:
        """Load mnemonics from a JSON file."""
        try:
            with open(file_path, "r") as f:
                data = json.load(f)
                self.mnemonics.update(data)
        except Exception as e:
            raise ValueError(f"Failed to load mnemonics from {file_path}: {e}")

    def get_mnemonic(self, name: str) -> Optional[dict]:
        """Get information about a specific mnemonic."""
        return self.mnemonics.get(name.upper())

    def get_by_category(self, category: str) -> Dict[str, dict]:
        """Get all mnemonics in a specific category."""
        return {
            name: info
            for name, info in self.mnemonics.items()
            if info.get("category") == category
        }

    def is_branch_instruction(self, mnemonic: str) -> bool:
        """Check if a mnemonic is a branch instruction."""
        info = self.get_mnemonic(mnemonic)
        return info is not None and info.get("category") == "BRANCH"

    def is_directive(self, mnemonic: str) -> bool:
        """Check if a mnemonic is a directive."""
        info = self.get_mnemonic(mnemonic)
        return info is not None and info.get("category") == "DIRECTIVE"
