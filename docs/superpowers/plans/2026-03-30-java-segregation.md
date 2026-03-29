# Java Build Segregation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Move all Java/Maven modules into a `java/` subdirectory and add a top-level Makefile for unified builds.

**Architecture:** `git mv` for regular directories, submodule deinit/re-add for `mojo-common` and `woof`, then update all cross-references in `.gitmodules`, CI, `CLAUDE.md`, `README.md`, LSP mnemonics path, and `.gitignore`.

**Tech Stack:** Git, Maven, Make, shell

---

### Task 1: Move Regular Java Directories

**Files:**
- Move: `pom.xml` → `java/pom.xml`
- Move: `hlasm-parser/` → `java/hlasm-parser/`
- Move: `hlasm-format-loader/` → `java/hlasm-format-loader/`
- Move: `hlasm-graph-loader/` → `java/hlasm-graph-loader/`
- Move: `tapez-cli/` → `java/tapez-cli/`
- Move: `tapez-mcp-server/` → `java/tapez-mcp-server/`
- Move: `.mvn/` → `java/.mvn/`
- Move: `gen/` → `java/gen/`
- Move: `hlasm-analyser.iml` → `java/hlasm-analyser.iml`

- [ ] **Step 1: Create java/ directory and move all non-submodule Java files**

```bash
cd /Users/asgupta/code/tape-z
mkdir -p java
git mv pom.xml java/
git mv hlasm-parser java/
git mv hlasm-format-loader java/
git mv hlasm-graph-loader java/
git mv tapez-cli java/
git mv tapez-mcp-server java/
git mv hlasm-analyser.iml java/
```

- [ ] **Step 2: Move .mvn/ and gen/ (may be gitignored -- use filesystem move + git add)**

`.mvn/` and `gen/` are in `.gitignore` (`.mvn/` via pattern, `**/gen/` via pattern). Since they're ignored, `git mv` won't work. Move them on the filesystem:

```bash
cd /Users/asgupta/code/tape-z
mv .mvn java/.mvn
mv gen java/gen
```

These are gitignored so they won't appear in the commit, but they need to exist on disk for Maven to work.

- [ ] **Step 3: Verify the moves**

```bash
cd /Users/asgupta/code/tape-z
ls java/pom.xml java/hlasm-parser/pom.xml java/tapez-cli/pom.xml
ls -d java/hlasm-format-loader java/hlasm-graph-loader java/tapez-mcp-server
```

Expected: All files/directories exist at their new paths.

- [ ] **Step 4: Commit**

```bash
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Moving Java modules into java/ subdirectory"
```

---

### Task 2: Move Git Submodules

**Files:**
- Modify: `.gitmodules`
- Move: `mojo-common/` → `java/mojo-common/`
- Move: `woof/` → `java/woof/`

- [ ] **Step 1: Record submodule URLs**

```bash
cd /Users/asgupta/code/tape-z
git config --file .gitmodules submodule.mojo-common.url
git config --file .gitmodules submodule.woof.url
```

Expected output:
```
https://github.com/asengupta/mojo-common
https://github.com/asengupta/woof
```

- [ ] **Step 2: Deinit and remove mojo-common**

```bash
cd /Users/asgupta/code/tape-z
git submodule deinit -f mojo-common
git rm -f mojo-common
rm -rf .git/modules/mojo-common
```

- [ ] **Step 3: Deinit and remove woof**

```bash
cd /Users/asgupta/code/tape-z
git submodule deinit -f woof
git rm -f woof
rm -rf .git/modules/woof
```

- [ ] **Step 4: Re-add submodules at new paths**

```bash
cd /Users/asgupta/code/tape-z
git submodule add https://github.com/asengupta/mojo-common java/mojo-common
git submodule add https://github.com/asengupta/woof java/woof
```

- [ ] **Step 5: Verify .gitmodules is correct**

```bash
cat .gitmodules
```

Expected:
```
[submodule "java/mojo-common"]
	path = java/mojo-common
	url = https://github.com/asengupta/mojo-common
[submodule "java/woof"]
	path = java/woof
	url = https://github.com/asengupta/woof
```

- [ ] **Step 6: Verify submodules are checked out**

```bash
ls java/mojo-common/pom.xml java/woof/pom.xml
git submodule status
```

Expected: Both submodules present and initialized.

- [ ] **Step 7: Commit**

```bash
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Moving git submodules into java/ subdirectory"
```

---

### Task 3: Verify Java Build

- [ ] **Step 1: Run Maven build from java/**

```bash
cd /Users/asgupta/code/tape-z/java
mvn clean test -Dmaven.test.skip=true
```

Expected: `BUILD SUCCESS`. If any module can't resolve siblings or submodules, the POM module paths may need adjusting -- but since they're relative within `java/` and the POM moved with them, they should work as-is.

- [ ] **Step 2: If build fails, debug**

The `pom.xml` lists modules as relative paths (`hlasm-parser`, `tapez-cli`, etc.). Since the POM and all modules moved together into `java/`, the relative paths are unchanged. If submodules fail, verify `java/mojo-common/pom.xml` and `java/woof/pom.xml` exist.

- [ ] **Step 3: Commit if any fixes were needed**

Only commit if changes were required. No commit if the build passed without modifications.

---

### Task 4: Update .gitignore

**Files:**
- Modify: `.gitignore`

- [ ] **Step 1: Add entries for root-level artifacts to ignore**

Add these lines to the end of `.gitignore`:

```
# Root-level build artifacts (legacy, before java/ segregation)
output.log
```

Note: `output/`, `*.dot`, `**/op.json`, and `*.log` are already covered by existing patterns in `.gitignore`. The existing patterns `output/`, `*.dot`, `**/gen/`, `**/op.json` already handle these files. No changes needed to `.gitignore` for those.

- [ ] **Step 2: Verify the ignored files are covered**

```bash
cd /Users/asgupta/code/tape-z
git status --short
```

Expected: `output/`, `flowchart.dot`, `program-flowchart.dot`, `op.json`, `output.log` should NOT appear as untracked (they're already covered by existing patterns).

- [ ] **Step 3: Commit if any changes were made**

```bash
git add .gitignore
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Updating .gitignore for java/ segregation"
```

---

### Task 5: Update CI Workflow

**Files:**
- Modify: `.github/workflows/build-and-publish.yml`

- [ ] **Step 1: Update the Maven build step**

In `.github/workflows/build-and-publish.yml`, change the build step from:

```yaml
      - name: Build with Maven
        run: mvn clean install -Dmaven.test.skip=true
```

to:

```yaml
      - name: Build with Maven
        run: cd java && mvn clean install -Dmaven.test.skip=true
```

- [ ] **Step 2: Commit**

```bash
git add .github/workflows/build-and-publish.yml
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Updating CI workflow for java/ subdirectory"
```

---

### Task 6: Update LSP Mnemonics Path

**Files:**
- Modify: `hlasm-lsp/hlasm_lsp/mnemonics.py`

- [ ] **Step 1: Update the relative path to the CSV**

In `hlasm-lsp/hlasm_lsp/mnemonics.py`, change:

```python
    return package_root.parent / "hlasm-parser" / "instruction_formats" / "HLASM Instruction Format.csv"
```

to:

```python
    return package_root.parent / "java" / "hlasm-parser" / "instruction_formats" / "HLASM Instruction Format.csv"
```

- [ ] **Step 2: Run LSP tests to verify**

```bash
cd /Users/asgupta/code/tape-z/hlasm-lsp
poetry run python -m pytest tests/unit/ -v
```

Expected: All 42 tests pass.

- [ ] **Step 3: Commit**

```bash
cd /Users/asgupta/code/tape-z
git add hlasm-lsp/hlasm_lsp/mnemonics.py
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Updating LSP mnemonics CSV path for java/ subdirectory"
```

---

### Task 7: Create Top-Level Makefile

**Files:**
- Create: `Makefile`

- [ ] **Step 1: Create the Makefile**

Create `Makefile` at the repo root:

```makefile
.PHONY: all grammar java test-lsp test clean fmt

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
```

**IMPORTANT:** Makefile rules must use tabs, not spaces, for indentation. Each indented line under a target must begin with a literal tab character.

- [ ] **Step 2: Test individual targets**

```bash
cd /Users/asgupta/code/tape-z
make grammar
```

Expected: tree-sitter generates and all tree-sitter tests pass.

```bash
make test-lsp
```

Expected: Black check passes (or reports files to format), all 42 pytest tests pass.

- [ ] **Step 3: Test the java target**

```bash
cd /Users/asgupta/code/tape-z
make java
```

Expected: Maven builds and runs tests (or skips tests if none configured). `BUILD SUCCESS`.

- [ ] **Step 4: Test make all**

```bash
cd /Users/asgupta/code/tape-z
make all
```

Expected: All three targets run in sequence and succeed.

- [ ] **Step 5: Commit**

```bash
git add Makefile
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Adding top-level Makefile for unified builds"
```

---

### Task 8: Update CLAUDE.md

**Files:**
- Modify: `CLAUDE.md`

- [ ] **Step 1: Update repository structure section**

Replace the `## Repository Structure` section with:

```markdown
## Repository Structure

- `java/` — All Java/Maven modules:
  - `java/hlasm-parser/` — ANTLR4-based HLASM parser
  - `java/hlasm-format-loader/` — Instruction format loading and processing
  - `java/hlasm-graph-loader/` — Control flow graph generation and analysis
  - `java/tapez-cli/` — Command-line interface
  - `java/tapez-mcp-server/` — Model Context Protocol server
  - `java/mojo-common/` — Shared algorithms (git submodule)
  - `java/woof/` — Neo4j + LLM integration (git submodule)
- `tree-sitter-hlasm/` — Tree-sitter grammar for HLASM (JavaScript/C)
- `hlasm-lsp/` — HLASM Language Server Protocol server (Python)
- `Makefile` — Unified build orchestration
```

- [ ] **Step 2: Update verification gate section**

Replace the Java verification gate from:

```markdown
#### Java modules

\`\`\`bash
./mvnw clean test           # compile and run all tests
\`\`\`
```

to:

```markdown
#### Java modules

\`\`\`bash
cd java && mvn clean test    # compile and run all tests
\`\`\`
```

Also add after the Tree-sitter grammar section:

```markdown
#### Unified build (all targets)

\`\`\`bash
make all                     # grammar + java + LSP tests
\`\`\`
```

- [ ] **Step 3: Update external dependencies section**

Replace:

```markdown
- `mojo-common/` and `woof/` are git submodules — run `git submodule update --init` after clone.
```

with:

```markdown
- `java/mojo-common/` and `java/woof/` are git submodules — run `git submodule update --init` after clone.
```

- [ ] **Step 4: Commit**

```bash
git add CLAUDE.md
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Updating CLAUDE.md for java/ subdirectory layout"
```

---

### Task 9: Update README.md

**Files:**
- Modify: `README.md`

- [ ] **Step 1: Update Prerequisites section**

After the existing prerequisites (Java 21, Maven 3.6, Neo4J), add:

```markdown
- Node.js (for tree-sitter grammar generation)
- Python 3.13+ and Poetry (for HLASM LSP server)
```

- [ ] **Step 2: Update Installation / Build section**

Replace the build step (line 70-73) from:

```markdown
2. Build the project:
   \`\`\`bash
   mvn clean install
   \`\`\`
```

to:

```markdown
2. Build the project:
   \`\`\`bash
   make all
   \`\`\`

   Or build individual components:
   \`\`\`bash
   make java        # Java modules only (includes tests)
   make grammar     # Tree-sitter HLASM grammar (includes tests)
   make test-lsp    # Python LSP server tests
   \`\`\`
```

- [ ] **Step 3: Update CLI usage paths**

Replace all occurrences of `tapez-cli/target/` with `java/tapez-cli/target/` in the CLI usage examples. There are three occurrences:

Line 130:
```
java -jar java/tapez-cli/target/tapez-cli-1.0-SNAPSHOT.jar cfg-to-json ...
```

Line 148:
```
OLLAMA_ENDPOINT=http://<ollama.endpoint> java -jar java/tapez-cli/target/tapez-cli-1.0-SNAPSHOT.jar flowchart ...
```

Line 170:
```
OLLAMA_ENDPOINT=http://<ollama.endpoint> java -jar java/tapez-cli/target/tapez-cli-1.0-SNAPSHOT.jar flowchart-sections ...
```

Line 178-179 (help commands):
```
java -jar java/tapez-cli/target/tapez-cli-1.0-SNAPSHOT.jar --help
```

Line 184:
```
java -jar java/tapez-cli/target/tapez-cli-1.0-SNAPSHOT.jar <command> --help
```

- [ ] **Step 4: Update Project Overview paragraph**

In the "Project Overview" section (around line 30), after the existing text, add a paragraph:

```markdown
The repository is organized into three ecosystems: Java modules (under `java/`), a tree-sitter grammar for HLASM syntax (`tree-sitter-hlasm/`), and a Python-based LSP server for editor integration (`hlasm-lsp/`). A top-level `Makefile` orchestrates builds across all three.
```

- [ ] **Step 5: Update Workflow section**

Replace the workflow section (lines 189-194) with:

```markdown
## Workflow

The typical workflow is:
1. HLASM code is parsed using the grammar from `java/hlasm-parser` and `java/hlasm-format-loader`
2. The parsed code is analysed by `java/hlasm-graph-loader` using algorithms from `java/mojo-common` to build control flow graphs
3. The analysis results are stored in Neo4J using the `java/woof` module
4. The `java/tapez-mcp-server` provides API access to the analysis capabilities and results
5. The `hlasm-lsp` server provides real-time editor support (diagnostics, completion, navigation)
```

- [ ] **Step 6: Commit**

```bash
git add README.md
git commit --author="avishek-sen-gupta <asgupta@thoughtworks.com>" -m "Updating README.md for java/ subdirectory layout and Makefile usage"
```

---

### Task 10: Final Verification and Push

- [ ] **Step 1: Verify repo structure**

```bash
cd /Users/asgupta/code/tape-z
ls java/pom.xml java/hlasm-parser/pom.xml java/tapez-cli/pom.xml
ls java/mojo-common/pom.xml java/woof/pom.xml
ls Makefile hlasm-lsp/pyproject.toml tree-sitter-hlasm/grammar.js
```

Expected: All files exist.

- [ ] **Step 2: Verify no Java files remain at root**

```bash
cd /Users/asgupta/code/tape-z
ls pom.xml 2>/dev/null && echo "ERROR: pom.xml still at root" || echo "OK: pom.xml moved"
ls -d hlasm-parser 2>/dev/null && echo "ERROR: hlasm-parser still at root" || echo "OK: hlasm-parser moved"
ls -d mojo-common 2>/dev/null && echo "ERROR: mojo-common still at root" || echo "OK: mojo-common moved"
ls -d woof 2>/dev/null && echo "ERROR: woof still at root" || echo "OK: woof moved"
```

Expected: All "OK".

- [ ] **Step 3: Run make grammar**

```bash
cd /Users/asgupta/code/tape-z
make grammar
```

Expected: tree-sitter generates and tests pass.

- [ ] **Step 4: Run make test-lsp**

```bash
cd /Users/asgupta/code/tape-z
make test-lsp
```

Expected: Black check passes, all 42 pytest tests pass.

- [ ] **Step 5: Run make java**

```bash
cd /Users/asgupta/code/tape-z
make java
```

Expected: `BUILD SUCCESS`.

- [ ] **Step 6: Push all commits**

```bash
git push
```
