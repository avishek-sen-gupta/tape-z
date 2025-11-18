"""File reading and processing utilities"""

from pathlib import Path
from typing import List, Optional


class LineProcessor:
    """Processes HLASM source lines following mainframe conventions."""

    @staticmethod
    def truncate_at_column_72(line: str) -> str:
        """Truncate line beyond column 72 following HLASM standards."""
        return line[:72] if len(line) > 72 else line

    @staticmethod
    def collapse_continued_lines(lines: List[str]) -> List[str]:
        """Collapse lines that are marked as continued into single logical lines."""
        result = []
        current_line = ""

        for line in lines:
            stripped = line.rstrip()
            # HLASM continuation character is typically in column 72
            if len(stripped) >= 72 and not stripped[71].isspace():
                current_line += stripped[:71]
            else:
                current_line += stripped
                if current_line:
                    result.append(current_line)
                current_line = ""

        if current_line:
            result.append(current_line)

        return result


class FileReader:
    """Reads HLASM source files."""

    def __init__(self, encoding: str = "utf-8"):
        self.encoding = encoding

    def read_lines(self, file_path: Path) -> List[str]:
        """Read all lines from a file."""
        with open(file_path, "r", encoding=self.encoding) as f:
            return f.readlines()

    def read_hlasm_file(self, file_path: Path) -> List[str]:
        """Read and preprocess HLASM file according to standards."""
        lines = self.read_lines(file_path)
        # Truncate at column 72
        truncated = [LineProcessor.truncate_at_column_72(line) for line in lines]
        # Collapse continued lines
        return LineProcessor.collapse_continued_lines(truncated)
