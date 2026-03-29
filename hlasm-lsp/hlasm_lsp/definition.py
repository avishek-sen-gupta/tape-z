from hlasm_lsp.index import DocumentIndex, SymbolLocation


def find_definition(index: DocumentIndex, line: int, character: int) -> SymbolLocation | None:
    sym = index.get_symbol_at(line, character)
    if sym is None:
        return None
    defs = index.get_definition(sym.name)
    if not defs:
        return None
    return defs[0]
