from lsprotocol import types
from hlasm_lsp.index import DocumentIndex


def get_document_symbols(index: DocumentIndex) -> list[types.DocumentSymbol]:
    if not index.sections:
        return _flat_symbols(index)

    result: list[types.DocumentSymbol] = []
    for section in index.sections:
        children = _labels_in_range(index, section.start_line, section.end_line, section.name)
        symbol_kind = (
            types.SymbolKind.Module if section.kind == "CSECT" else types.SymbolKind.Struct
        )
        result.append(
            types.DocumentSymbol(
                name=section.name,
                kind=symbol_kind,
                range=types.Range(
                    start=types.Position(line=section.start_line, character=0),
                    end=types.Position(line=section.end_line, character=0),
                ),
                selection_range=types.Range(
                    start=types.Position(line=section.start_line, character=0),
                    end=types.Position(line=section.start_line, character=len(section.name)),
                ),
                children=children,
            )
        )
    return result


def _labels_in_range(
    index: DocumentIndex, start: int, end: int, exclude_name: str
) -> list[types.DocumentSymbol]:
    children: list[types.DocumentSymbol] = []
    for name, locs in index.definitions.items():
        if name == exclude_name:
            continue
        for loc in locs:
            if start <= loc.line <= end:
                children.append(
                    types.DocumentSymbol(
                        name=name,
                        kind=types.SymbolKind.Constant,
                        range=types.Range(
                            start=types.Position(line=loc.line, character=loc.character),
                            end=types.Position(line=loc.line, character=loc.end_character),
                        ),
                        selection_range=types.Range(
                            start=types.Position(line=loc.line, character=loc.character),
                            end=types.Position(line=loc.line, character=loc.end_character),
                        ),
                    )
                )
    children.sort(key=lambda s: s.range.start.line)
    return children


def _flat_symbols(index: DocumentIndex) -> list[types.DocumentSymbol]:
    result: list[types.DocumentSymbol] = []
    for name, locs in index.definitions.items():
        for loc in locs:
            result.append(
                types.DocumentSymbol(
                    name=name,
                    kind=types.SymbolKind.Constant,
                    range=types.Range(
                        start=types.Position(line=loc.line, character=loc.character),
                        end=types.Position(line=loc.line, character=loc.end_character),
                    ),
                    selection_range=types.Range(
                        start=types.Position(line=loc.line, character=loc.character),
                        end=types.Position(line=loc.line, character=loc.end_character),
                    ),
                )
            )
    result.sort(key=lambda s: s.range.start.line)
    return result
