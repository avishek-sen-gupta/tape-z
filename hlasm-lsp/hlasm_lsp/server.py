import logging

from lsprotocol import types
from pygls.lsp.server import LanguageServer

from hlasm_lsp.parser import HlasmParser
from hlasm_lsp.index import DocumentIndex
from hlasm_lsp.diagnostics import extract_diagnostics

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

    server._hlasm_parser = parser
    server._hlasm_documents = documents

    return server
