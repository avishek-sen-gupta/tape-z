import csv
import os
from dataclasses import dataclass
from pathlib import Path


@dataclass(frozen=True)
class MnemonicInfo:
    mnemonic: str
    operands: str
    format: str
    opcode: str
    description: str


def _find_csv_path() -> Path:
    env_path = os.environ.get("HLASM_MNEMONICS_CSV")
    if env_path:
        return Path(env_path)
    package_root = Path(__file__).resolve().parent.parent
    return (
        package_root.parent
        / "hlasm-parser"
        / "instruction_formats"
        / "HLASM Instruction Format.csv"
    )


def load_mnemonics() -> dict[str, MnemonicInfo]:
    csv_path = _find_csv_path()
    result: dict[str, MnemonicInfo] = {}
    with open(csv_path, newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            mnemonic = row["Mnemonic"].strip()
            if not mnemonic:
                continue
            info = MnemonicInfo(
                mnemonic=mnemonic,
                operands=row.get("Operands", "").strip(),
                format=row.get("Fmt", "").strip(),
                opcode=row.get("Opcd", "").strip(),
                description=row.get("Instruction", "").strip(),
            )
            result[mnemonic] = info
    return result


ASSEMBLER_DIRECTIVES: set[str] = {
    "ACONTROL",
    "ADATA",
    "AEJECT",
    "AGO",
    "AGOB",
    "AIF",
    "AIFB",
    "AINSERT",
    "ALIAS",
    "AMODE",
    "ANOP",
    "AREAD",
    "ASPACE",
    "CATTR",
    "CCW",
    "CCW0",
    "CCW1",
    "CNOP",
    "COM",
    "COPY",
    "CSECT",
    "CXD",
    "DC",
    "DROP",
    "DS",
    "DSECT",
    "DXD",
    "EJECT",
    "END",
    "ENTRY",
    "EQU",
    "EXITCTL",
    "EXTRN",
    "GBLA",
    "GBLB",
    "GBLC",
    "ICTL",
    "ISEQ",
    "LCLA",
    "LCLB",
    "LCLC",
    "LOCTR",
    "LTORG",
    "MACRO",
    "MEND",
    "MEXIT",
    "MHELP",
    "MNOTE",
    "OPSYN",
    "ORG",
    "POP",
    "PRINT",
    "PUNCH",
    "PUSH",
    "REPRO",
    "RMODE",
    "RSECT",
    "SETA",
    "SETAF",
    "SETB",
    "SETC",
    "SETCF",
    "SPACE",
    "START",
    "TITLE",
    "USING",
    "WXTRN",
    "XATTR",
}

DC_TYPE_DESCRIPTIONS: dict[str, str] = {
    "A": "Address constant (fullword, 4 bytes)",
    "AD": "Address constant (doubleword, 8 bytes)",
    "B": "Binary constant",
    "C": "Character constant (EBCDIC)",
    "CA": "Character constant (ASCII)",
    "CE": "Character constant (EBCDIC, explicit)",
    "CU": "Character constant (Unicode UTF-16)",
    "D": "Long floating-point constant (doubleword, 8 bytes)",
    "DH": "Long hexadecimal floating-point constant",
    "DB": "Long binary floating-point constant",
    "DD": "Long decimal floating-point constant",
    "E": "Short floating-point constant (fullword, 4 bytes)",
    "EH": "Short hexadecimal floating-point constant",
    "EB": "Short binary floating-point constant",
    "ED": "Short decimal floating-point constant",
    "F": "Fullword fixed-point constant (4 bytes)",
    "FD": "Doubleword fixed-point constant (8 bytes)",
    "G": "Graphic (DBCS) constant",
    "H": "Halfword fixed-point constant (2 bytes)",
    "J": "J-type address constant",
    "P": "Packed decimal constant",
    "Q": "Q-type offset constant (DXD)",
    "R": "R-type address constant",
    "S": "S-type address constant (base-displacement)",
    "V": "V-type address constant (external symbol)",
    "X": "Hexadecimal constant",
    "Y": "Y-type address constant (halfword)",
    "Z": "Zoned decimal constant",
}
