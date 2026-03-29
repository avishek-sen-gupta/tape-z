.PHONY: all grammar java test test-lsp clean fmt

all: grammar java test-lsp

grammar:
	cd tree-sitter-hlasm && npx tree-sitter generate && npx tree-sitter test

java:
	cd java && mvn clean test

test: all

test-lsp:
	cd hlasm-lsp && poetry run python -m black --check . && poetry run python -m pytest tests/ -v

clean:
	cd java && mvn clean
	rm -rf tree-sitter-hlasm/src/parser.c tree-sitter-hlasm/src/tree_sitter
	cd hlasm-lsp && rm -rf .pytest_cache __pycache__

fmt:
	cd hlasm-lsp && poetry run python -m black .
