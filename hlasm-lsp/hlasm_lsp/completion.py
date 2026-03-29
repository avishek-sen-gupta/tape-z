from lsprotocol import types
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.mnemonics import load_mnemonics, ASSEMBLER_DIRECTIVES, DC_TYPE_DESCRIPTIONS

_cached_mnemonic_items: list[types.CompletionItem] | None = None
_cached_register_items: list[types.CompletionItem] | None = None


def _mnemonic_items() -> list[types.CompletionItem]:
    global _cached_mnemonic_items
    if _cached_mnemonic_items is not None:
        return _cached_mnemonic_items
    items: list[types.CompletionItem] = []
    mnemonics = load_mnemonics()
    for name, info in mnemonics.items():
        detail = info.description if info.description else info.format
        items.append(
            types.CompletionItem(
                label=name,
                kind=types.CompletionItemKind.Function,
                detail=detail,
                insert_text=name,
            )
        )
    for directive in sorted(ASSEMBLER_DIRECTIVES):
        if directive not in mnemonics:
            items.append(
                types.CompletionItem(
                    label=directive,
                    kind=types.CompletionItemKind.Keyword,
                    detail="Assembler directive",
                    insert_text=directive,
                )
            )
    _cached_mnemonic_items = items
    return items


def _register_items() -> list[types.CompletionItem]:
    global _cached_register_items
    if _cached_register_items is not None:
        return _cached_register_items
    items = [
        types.CompletionItem(
            label=f"R{i}",
            kind=types.CompletionItemKind.Variable,
            detail=f"General register {i}",
            insert_text=f"R{i}",
        )
        for i in range(16)
    ]
    _cached_register_items = items
    return items


def get_completions(index: DocumentIndex) -> list[types.CompletionItem]:
    items: list[types.CompletionItem] = []
    items.extend(_mnemonic_items())
    items.extend(_register_items())
    for name in index.definitions:
        items.append(
            types.CompletionItem(
                label=name,
                kind=types.CompletionItemKind.Reference,
                detail="Label",
                insert_text=name,
            )
        )
    for type_letter, desc in DC_TYPE_DESCRIPTIONS.items():
        items.append(
            types.CompletionItem(
                label=type_letter,
                kind=types.CompletionItemKind.TypeParameter,
                detail=desc,
                insert_text=type_letter,
            )
        )
    return items
