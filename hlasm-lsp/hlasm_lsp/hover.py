from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.mnemonics import load_mnemonics, ASSEMBLER_DIRECTIVES, DC_TYPE_DESCRIPTIONS

_mnemonics: dict | None = None


def _get_mnemonics() -> dict:
    global _mnemonics
    if _mnemonics is None:
        _mnemonics = load_mnemonics()
    return _mnemonics


def get_hover_info(index: DocumentIndex, line: int, character: int) -> str | None:
    sym = index.get_symbol_at(line, character)
    if sym is None:
        return None

    name = sym.name
    name_upper = name.upper()

    if name in index.equ_values:
        return f"**{name}** EQU {index.equ_values[name]}"

    for key, val in index.equ_values.items():
        if key.upper() == name_upper:
            return f"**{key}** EQU {val}"

    mnemonics = _get_mnemonics()
    if name_upper in mnemonics:
        info = mnemonics[name_upper]
        parts = [f"**{info.mnemonic}**"]
        if info.description:
            parts.append(info.description)
        if info.format:
            parts.append(f"Format: {info.format}")
        if info.operands:
            parts.append(f"Operands: `{info.operands}`")
        return "\n\n".join(parts)

    if name_upper in ASSEMBLER_DIRECTIVES:
        return f"**{name_upper}** — Assembler directive"

    if name_upper in DC_TYPE_DESCRIPTIONS:
        return f"**{name_upper}** — {DC_TYPE_DESCRIPTIONS[name_upper]}"

    defs = index.get_definition(name)
    if defs:
        loc = defs[0]
        return f"**{name}** — Label defined at line {loc.line + 1}"

    return None
