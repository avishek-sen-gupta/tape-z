from hlasm_lsp.index import DocumentIndex, SymbolLocation


def find_references(
    index: DocumentIndex, line: int, character: int, include_definition: bool = True
) -> list[SymbolLocation]:
    sym = index.get_symbol_at(line, character)
    if sym is None:
        return []
    result: list[SymbolLocation] = []
    if include_definition:
        result.extend(index.get_definition(sym.name))
    result.extend(index.get_references(sym.name))
    return result
