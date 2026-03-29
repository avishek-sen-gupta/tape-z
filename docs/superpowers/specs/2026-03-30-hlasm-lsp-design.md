# HLASM LSP Server Design

## Overview

A Python-based Language Server Protocol (LSP) server for IBM HLASM, built on the tree-sitter grammar in `tree-sitter-hlasm/`. Provides syntax diagnostics, highlighting, navigation, hover, completion, and code folding. Targets VS Code as the primary editor with a minimal extension wrapper.

## Project Structure

```
hlasm-lsp/                          # Top-level Poetry project
├── pyproject.toml                  # Poetry config, dependencies
├── hlasm_lsp/
│   ├── __init__.py
│   ├── __main__.py                 # Entry point: python -m hlasm_lsp
│   ├── server.py                   # pygls LSP server, capability registration
│   ├── parser.py                   # tree-sitter integration, document parsing
│   ├── index.py                    # Symbol index: defs, refs, per-document state
│   ├── diagnostics.py              # Parse error extraction, diagnostic publishing
│   ├── symbols.py                  # Document symbols / outline provider
│   ├── definition.py               # Go-to-definition provider
│   ├── references.py               # Find-references provider
│   ├── hover.py                    # Hover info provider
│   ├── completion.py               # Completion provider
│   ├── semantic_tokens.py          # Semantic token provider
│   ├── folding.py                  # Code folding provider
│   └── mnemonics.py                # HLASM instruction mnemonic data
└── vscode-extension/
    ├── package.json                # VS Code extension manifest
    └── extension.js                # Minimal LSP client launcher
```

## Dependencies

Managed with Poetry:

- **pygls** (>=2.0) -- LSP protocol implementation
- **lsprotocol** -- LSP type definitions (transitive via pygls)
- **tree-sitter** (>=0.22) -- Python tree-sitter bindings
- Dev: **pytest**, **pytest-asyncio**

## Grammar Integration

The server loads the tree-sitter HLASM grammar at startup by compiling `parser.c` from the sibling directory `../tree-sitter-hlasm/src/`. The path can be overridden with the `HLASM_GRAMMAR_PATH` environment variable.

## Symbol Index (`index.py`)

Central data structure, rebuilt per-document on every edit.

### Per-document state

- **Parse tree** -- the tree-sitter tree for the current document version
- **Definitions** -- `dict[str, list[Location]]`: labels, EQU names, CSECT/DSECT names, MACRO names
- **References** -- `dict[str, list[Location]]`: all symbol usages in operands
- **Sections** -- ordered list of CSECT/DSECT/RSECT boundaries with ranges
- **Macro blocks** -- list of MACRO...MEND ranges

### Tree-walking strategy

A single post-parse walk extracts all information:

- `label` field on `instruction_statement` → **definition**
- `symbol` inside `operands` → **reference**
- `operation` field → classified against mnemonic table (for hover/completion) or recognized as macro call
- Section-starting operations (CSECT, DSECT, RSECT) → **section boundary**
- MACRO/MEND operations → **macro block range**

### Future COPY support

The index is keyed by document URI. Today only open documents are indexed. Adding COPY resolution later requires:

1. A file resolver that searches the workspace for copybook files
2. Parsing and indexing resolved copybooks into the same index
3. Cross-document `get_definition()` / `get_references()` lookups -- the API already supports this by accepting any URI

No structural changes to the index are needed.

## LSP Capabilities

### 1. Diagnostics

- Triggered on `textDocument/didOpen` and `textDocument/didChange`
- Parse the document with tree-sitter
- Walk the tree for `ERROR` nodes
- Convert each to an LSP `Diagnostic` with range and descriptive message
- Publish via `textDocument/publishDiagnostics`

### 2. Semantic Tokens

Token types registered in the legend:

| Token Type   | Applies To                                      |
|-------------|--------------------------------------------------|
| `keyword`   | Assembler directives (CSECT, USING, EQU, DC, DS) |
| `function`  | Machine instruction mnemonics (LR, MVC, BAL)     |
| `variable`  | Ordinary symbols in operands                     |
| `parameter` | Variable symbols (&name)                         |
| `label`     | Labels and sequence symbols (.name)              |
| `number`    | Decimal numbers, self-defining terms             |
| `string`    | String literals, DC/DS values                    |
| `comment`   | Comment lines, macro comments                    |
| `type`      | DC/DS type specifications                        |
| `operator`  | Arithmetic operators (+, -, *, /)                |

Walk the parse tree and emit delta-encoded token data per the LSP semantic tokens protocol.

### 3. Go to Definition

- Find the symbol name under the cursor from the parse tree
- Look up in the index's definition map
- Return the definition location

### 4. Find References

- Find the symbol name under the cursor
- Return all reference locations plus the definition location

### 5. Hover

Context-dependent information:

- **Label defined via EQU**: show the EQU value expression
- **Known machine mnemonic**: show instruction format and description from `mnemonics.py`
- **DC/DS type letter**: show type meaning (e.g., F = fullword, H = halfword, CL80 = 80-byte character)
- **Assembler directive**: show brief description

### 6. Document Symbols

Hierarchical outline:

- Top level: CSECT/DSECT sections
- Children: labels defined within each section
- Standalone labels (before any section) at top level

### 7. Completion

Context-aware suggestions:

- **After operation field**: machine instruction mnemonics, assembler directives
- **In operands**: defined labels from the index, register names (R0-R15)
- **After DC/DS type position**: type letters (A, B, C, D, E, F, G, H, P, X, etc.) with descriptions

### 8. Code Folding

Foldable ranges:

- MACRO...MEND blocks
- Consecutive comment lines (3+ lines)
- CSECT/DSECT sections (from section start to next section or END)

## Mnemonic Data (`mnemonics.py`)

A dictionary of HLASM instruction mnemonics with:

- **Format**: RR, RX, RS, SI, SS, etc.
- **Description**: brief one-line description
- **Operand pattern**: e.g., "R1,D2(X2,B2)" for RX format

Sourced from the existing `hlasm-parser/instruction_formats/HLASM Instruction Format.csv` file in the repo. Loaded at startup.

## VS Code Extension

### `package.json`

- Extension ID: `hlasm-lsp`
- Language contribution: `hlasm` language ID
- File extensions: `.hlasm`, `.asm`, `.mac`, `.copy`, `.s`
- Settings: `hlasmLsp.pythonPath` (default: `python3`)
- Activation: on language `hlasm`

### `extension.js`

- Uses `vscode-languageclient` npm package
- Spawns the LSP server via `<pythonPath> -m hlasm_lsp` with stdio transport
- Connects the client to the spawned server process
- Handles server restart on crash

### Server entry point (`__main__.py`)

Starts the pygls server on stdio:

```python
from hlasm_lsp.server import create_server

server = create_server()
server.start_io()
```

## Configuration

| Setting | Default | Description |
|---------|---------|-------------|
| `hlasmLsp.pythonPath` | `python3` | Path to Python interpreter |
| `HLASM_GRAMMAR_PATH` (env) | `../tree-sitter-hlasm/src/` relative to package | Override grammar location |

## Task Tracking

Work tracked in Beads with dependency tracking between tasks. Major work items:

1. **Project scaffolding** -- Poetry project, pyproject.toml, entry point
2. **Parser integration** -- tree-sitter grammar loading, document parsing (depends on 1)
3. **Symbol index** -- definition/reference extraction, per-document state (depends on 2)
4. **Diagnostics** -- ERROR node extraction and publishing (depends on 2)
5. **Semantic tokens** -- token classification and emission (depends on 2)
6. **Go to definition** -- cursor-to-definition lookup (depends on 3)
7. **Find references** -- cursor-to-references lookup (depends on 3)
8. **Hover** -- context-dependent info display (depends on 3)
9. **Document symbols** -- outline provider (depends on 3)
10. **Completion** -- context-aware suggestions (depends on 3)
11. **Code folding** -- foldable range detection (depends on 3)
12. **Mnemonic data** -- load instruction formats from CSV (depends on 1)
13. **VS Code extension** -- launcher and language contribution (depends on 1)

Dependency chain: 1 → 2 → 3 → {6, 7, 8, 9, 10, 11}; 1 → {4, 5, 12, 13} are independent of the index.
