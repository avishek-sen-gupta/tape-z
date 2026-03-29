import logging

from lsprotocol import types
from pygls.lsp.server import LanguageServer

from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.diagnostics import extract_diagnostics
from hlasm_lsp.semantic_tokens import collect_semantic_tokens, TOKEN_TYPES, TOKEN_MODIFIERS

logger = logging.getLogger(__name__)

SERVER_NAME = "hlasm-lsp"
SERVER_VERSION = "0.1.0"


def create_server() -> LanguageServer:
    server = LanguageServer(SERVER_NAME, SERVER_VERSION)
    parser = HlasmParser()
    documents: dict[str, DocumentIndex] = {}

    def _reindex(uri: str, source: str) -> DocumentIndex:
        tree = parser.parse(source)
        index = DocumentIndex.from_tree(tree, uri)
        documents[uri] = index
        return index

    def _publish_diagnostics(ls: LanguageServer, uri: str, index: DocumentIndex) -> None:
        diagnostics = extract_diagnostics(index.tree)
        ls.text_document_publish_diagnostics(
            types.PublishDiagnosticsParams(uri=uri, diagnostics=diagnostics)
        )

    @server.feature(types.TEXT_DOCUMENT_DID_OPEN)
    def did_open(params: types.DidOpenTextDocumentParams) -> None:
        uri = params.text_document.uri
        source = params.text_document.text
        index = _reindex(uri, source)
        _publish_diagnostics(server, uri, index)

    @server.feature(types.TEXT_DOCUMENT_DID_CHANGE)
    def did_change(params: types.DidChangeTextDocumentParams) -> None:
        uri = params.text_document.uri
        source = server.workspace.get_text_document(uri).source
        index = _reindex(uri, source)
        _publish_diagnostics(server, uri, index)

    @server.feature(types.TEXT_DOCUMENT_DID_CLOSE)
    def did_close(params: types.DidCloseTextDocumentParams) -> None:
        uri = params.text_document.uri
        documents.pop(uri, None)

    legend = types.SemanticTokensLegend(
        token_types=TOKEN_TYPES,
        token_modifiers=TOKEN_MODIFIERS,
    )

    @server.feature(
        types.TEXT_DOCUMENT_SEMANTIC_TOKENS_FULL,
        types.SemanticTokensOptions(legend=legend, full=True),
    )
    def semantic_tokens_full(params: types.SemanticTokensParams) -> types.SemanticTokens:
        uri = params.text_document.uri
        index = documents.get(uri)
        if index is None:
            return types.SemanticTokens(data=[])
        tokens = collect_semantic_tokens(index.tree)
        data: list[int] = []
        prev_line = 0
        prev_char = 0
        for token in tokens:
            delta_line = token.line - prev_line
            delta_char = token.character if delta_line > 0 else token.character - prev_char
            type_index = TOKEN_TYPES.index(token.token_type)
            data.extend([delta_line, delta_char, token.length, type_index, 0])
            prev_line = token.line
            prev_char = token.character
        return types.SemanticTokens(data=data)

    from hlasm_lsp.definition import find_definition

    @server.feature(types.TEXT_DOCUMENT_DEFINITION)
    def goto_definition(params: types.DefinitionParams) -> types.Location | None:
        uri = params.text_document.uri
        index = documents.get(uri)
        if index is None:
            return None
        loc = find_definition(index, params.position.line, params.position.character)
        if loc is None:
            return None
        return types.Location(
            uri=loc.uri,
            range=types.Range(
                start=types.Position(line=loc.line, character=loc.character),
                end=types.Position(line=loc.line, character=loc.end_character),
            ),
        )

    from hlasm_lsp.references import find_references as _find_references

    @server.feature(types.TEXT_DOCUMENT_REFERENCES)
    def references(params: types.ReferenceParams) -> list[types.Location]:
        uri = params.text_document.uri
        index = documents.get(uri)
        if index is None:
            return []
        locs = _find_references(
            index,
            params.position.line,
            params.position.character,
            include_definition=params.context.include_declaration,
        )
        return [
            types.Location(
                uri=loc.uri,
                range=types.Range(
                    start=types.Position(line=loc.line, character=loc.character),
                    end=types.Position(line=loc.line, character=loc.end_character),
                ),
            )
            for loc in locs
        ]

    from hlasm_lsp.hover import get_hover_info

    @server.feature(types.TEXT_DOCUMENT_HOVER)
    def hover(params: types.HoverParams) -> types.Hover | None:
        uri = params.text_document.uri
        index = documents.get(uri)
        if index is None:
            return None
        info = get_hover_info(index, params.position.line, params.position.character)
        if info is None:
            return None
        return types.Hover(
            contents=types.MarkupContent(
                kind=types.MarkupKind.Markdown,
                value=info,
            )
        )

    server._hlasm_parser = parser
    server._hlasm_documents = documents

    return server
