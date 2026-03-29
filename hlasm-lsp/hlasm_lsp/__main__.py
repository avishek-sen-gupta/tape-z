import logging

from hlasm_lsp.server import create_server

logging.basicConfig(level=logging.INFO)


def main() -> None:
    server = create_server()
    server.start_io()


if __name__ == "__main__":
    main()
