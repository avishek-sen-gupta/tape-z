# Segregate Java/Maven Build into `java/` Subdirectory

## Overview

Move all Java/Maven modules, git submodules, and related build infrastructure from the repo root into a `java/` subdirectory. Add a top-level `Makefile` that orchestrates builds across all language ecosystems (Java, tree-sitter, Python LSP).

## What Moves into `java/`

| Current location | New location |
|---|---|
| `pom.xml` | `java/pom.xml` |
| `hlasm-parser/` | `java/hlasm-parser/` |
| `hlasm-format-loader/` | `java/hlasm-format-loader/` |
| `hlasm-graph-loader/` | `java/hlasm-graph-loader/` |
| `tapez-cli/` | `java/tapez-cli/` |
| `tapez-mcp-server/` | `java/tapez-mcp-server/` |
| `mojo-common/` (submodule) | `java/mojo-common/` |
| `woof/` (submodule) | `java/woof/` |
| `.mvn/` | `java/.mvn/` |
| `gen/` | `java/gen/` |
| `hlasm-analyser.iml` | `java/hlasm-analyser.iml` |

## What Stays at Root

- `CLAUDE.md`, `README.md`, `.gitignore`, `.talismanrc`, `.gitmodules`
- `.github/`, `.idea/`
- `docs/`, `documentation/`, `env/`
- `tree-sitter-hlasm/`, `hlasm-lsp/`
- `Makefile` (new)

## What Gets Gitignored

Add to `.gitignore` (not deleted from disk, just ignored going forward):

- `output/`
- `output.log`
- `flowchart.dot`
- `program-flowchart.dot`
- `op.json`

## Files Requiring Content Updates

### `.gitmodules`

Paths change:

- `woof`: `path = woof` → `path = java/woof`
- `mojo-common`: `path = mojo-common` → `path = java/mojo-common`

### `.github/workflows/build-and-publish.yml`

Maven command changes from `mvn clean install -Dmaven.test.skip=true` to run from `java/` directory:

```yaml
- name: Build with Maven
  run: cd java && mvn clean install -Dmaven.test.skip=true
```

### `hlasm-lsp/hlasm_lsp/mnemonics.py`

Relative path to instruction format CSV changes:

```python
# Before
package_root.parent / "hlasm-parser" / "instruction_formats" / "HLASM Instruction Format.csv"

# After
package_root.parent / "java" / "hlasm-parser" / "instruction_formats" / "HLASM Instruction Format.csv"
```

### `CLAUDE.md`

- Repository structure section updated to reflect `java/` subdirectory
- Verification gate Java command changes to `cd java && mvn clean test`
- Add Makefile targets documentation

### `README.md`

Update to reflect the new layout:

- Repository structure section rewritten to show `java/` subdirectory
- Build instructions updated to reference `Makefile` targets
- Module path references updated (e.g., `hlasm-parser/` → `java/hlasm-parser/`)

## Git Submodule Move Strategy

Git submodules cannot be moved with `git mv`. The procedure is:

1. Deinit each submodule: `git submodule deinit -f <path>`
2. Remove from index: `git rm -f <path>`
3. Remove from `.git/modules/`: `rm -rf .git/modules/<name>`
4. Re-add at new path: `git submodule add <url> java/<name>`

This preserves the submodule relationship with the upstream repos.

## Top-Level Makefile

Targets:

```makefile
all:       grammar java test-lsp    # default: build everything
grammar:   tree-sitter generate + test
java:      cd java && mvn clean test
test:      grammar java test-lsp    # all tests
test-lsp:  cd hlasm-lsp && poetry run black --check . && poetry run pytest tests/
clean:     clean all build artifacts
fmt:       cd hlasm-lsp && poetry run black .
```

Key design decisions:

- `make java` always runs tests (`mvn clean test`, not `install`)
- `make grammar` always runs tests (`tree-sitter generate && tree-sitter test`)
- `make all` runs grammar → java → test-lsp in sequence
- `make test` is an alias for `make all` (since all build targets include tests)

## Resulting Root Layout

```
tape-z/
├── Makefile                    # unified build orchestration
├── CLAUDE.md
├── README.md
├── .gitignore
├── .gitmodules
├── .github/workflows/
├── docs/
├── documentation/
├── env/
├── java/                       # all Java/Maven modules
│   ├── pom.xml
│   ├── .mvn/
│   ├── gen/
│   ├── hlasm-parser/
│   ├── hlasm-format-loader/
│   ├── hlasm-graph-loader/
│   ├── tapez-cli/
│   ├── tapez-mcp-server/
│   ├── mojo-common/            # git submodule
│   └── woof/                   # git submodule
├── tree-sitter-hlasm/          # tree-sitter grammar
└── hlasm-lsp/                  # Python LSP server
```
