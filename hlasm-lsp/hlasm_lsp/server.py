import logging

from lsprotocol import types
from pygls.lsp.server import LanguageServer

logger = logging.getLogger(__name__)

SERVER_NAME = "hlasm-lsp"
SERVER_VERSION = "0.1.0"


def create_server() -> LanguageServer:
    server = LanguageServer(SERVER_NAME, SERVER_VERSION)

    @server.feature(types.TEXT_DOCUMENT_DID_OPEN)
    def did_open(params: types.DidOpenTextDocumentParams) -> None:
        logger.info("Opened %s", params.text_document.uri)

    @server.feature(types.TEXT_DOCUMENT_DID_CHANGE)
    def did_change(params: types.DidChangeTextDocumentParams) -> None:
        logger.info("Changed %s", params.text_document.uri)

    return server
