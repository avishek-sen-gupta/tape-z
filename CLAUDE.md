# Tape/Z — Agent Instructions

## Project Context

- **Languages:** Java 21+ (main codebase), Python 3.13+ (LSP server), JavaScript (tree-sitter grammar, VS Code extension), Markdown (docs)
- **Java build:** Maven (`mvn` / `./mvnw`)
- **Python package manager:** Poetry (`poetry run` prefix for all Python commands)
- **Java test framework:** JUnit / Maven Surefire
- **Python test framework:** pytest
- **Formatter (Python):** Black
- **Pre-commit hooks:** Talisman (secret detection)
- **Issue tracker:** Beads (`bd`)
- **ADRs:** `docs/architectural-design-decisions.md`
- **Specs (immutable):** `docs/superpowers/specs/` and `docs/superpowers/plans/` — never modify these. Newer specs supersede older ones by convention.

## Repository Structure

- `hlasm-parser/` — ANTLR4-based HLASM parser (Java)
- `hlasm-format-loader/` — Instruction format loading and processing (Java)
- `hlasm-graph-loader/` — Control flow graph generation and analysis (Java)
- `tapez-cli/` — Command-line interface (Java)
- `tapez-mcp-server/` — Model Context Protocol server (Java)
- `mojo-common/` — Shared algorithms (git submodule)
- `woof/` — Neo4j + LLM integration (git submodule)
- `tree-sitter-hlasm/` — Tree-sitter grammar for HLASM (JavaScript/C)
- `hlasm-lsp/` — HLASM Language Server Protocol server (Python)

## Task Tracking

Use `bd` (Beads) for ALL task tracking. Do NOT use markdown TODO lists.

1. File an issue before starting work: `bd create "title" --description="..." -t bug|feature|task -p 0-4`
   - **Exhaustive details required.** The description must include enough context for someone (or a future agent) to understand the problem and approach without re-reading the surrounding code. Include: what is wrong or missing, where in the codebase it manifests, and any known constraints.
   - If the brainstorm or planning phase yields pertinent extra detail (trade-offs considered, rejected approaches, edge cases discovered, related issues), add that to the Beads issue as well.
2. Claim it: `bd update <id> --claim`
3. When done: `bd close <id> --reason "..."`
4. Before every commit: `bd backup`

## Workflow

### Phases (mandatory, in order)

Every non-trivial task goes through these phases. Do not skip. Do not start implementing before completing brainstorm.

1. **Brainstorm** — Read the relevant code. Check how the existing system handles similar cases. Identify at least two approaches and their trade-offs. Ask: "does the system already have infrastructure for this?" Consider whether an open-source project already solves the problem.
2. **Plan** — Choose an approach. For features spanning multiple modules, identify independently-committable units and their order. For Heavy tasks, write the design down before proceeding.
3. **Test first** — Write failing tests that define the expected behavior. No implementation code until at least one test exists.
4. **Implement** — Write the minimum code to make the tests pass.
5. **Self-review** — Before running the verification gate, review your own diff (`git diff`). Check against the Design Principles and Programming Patterns sections below. Look for: workaround guards, mutation in loops, missing test coverage, weak assertions, leaked abstractions, stale docs. If the diff is large (Heavy task), run the `/review` skill.
6. **Verify** — Run the full verification gate (see below). All checks must pass.
7. **Commit** — One logical unit per commit. `bd backup` before `git add`. Push to remote.

When asked to audit or show issues, only report findings — do not fix unless explicitly asked.

### Complexity classification

Classify before starting. This determines how much ceremony is needed.

- **Light** (< 50 lines, single file, no new abstractions) — brief brainstorm. Example: adding an instruction to a dispatch table.
- **Standard** (50–300 lines, 2–5 files, follows existing patterns) — brainstorm identifies the pattern being followed. Example: adding a new LSP provider.
- **Heavy** (300+ lines, new abstractions, multiple subsystems) — brainstorm must produce a written design with trade-offs before any code. Break into independently-committable units. Do not attempt in a single pass. Re-read actual code before each phase — design documents can anchor you to a flawed model.

### Verification gate

#### Java modules

```bash
./mvnw clean test           # compile and run all tests
```

#### Python modules (hlasm-lsp)

Run all three before every commit, in this order:

```bash
poetry run python -m black .         # formatting
poetry run python -m pytest tests/   # all tests
```

#### Tree-sitter grammar

```bash
cd tree-sitter-hlasm && npx tree-sitter generate && npx tree-sitter test
```

Do not commit if any check fails. Fix, then re-run. Non-negotiable.

### Commits and state

- One logical unit per commit. Each commit must have its own tests.
- Push to `main` unless otherwise instructed.
- Update README and other living docs (ADRs, etc.) if the diff changes public behavior, adds features, or modifies architecture. This is part of the commit, not a follow-up.
- `bd backup` before every commit.
- Leave the working directory clean. No uncommitted files.
- Prefer a committed partial result over an uncommitted complete attempt. If a session may end, commit what's done with a `WIP:` prefix and file an issue for the remainder.
- When test counts are mentioned, verify that count hasn't regressed.

### Documentation

- Record salient architectural decisions as timestamped ADRs in `docs/architectural-design-decisions.md`.
- Never modify files in `docs/superpowers/specs/` or `docs/superpowers/plans/`.

## Design Principles

- **Use existing infrastructure before adding new abstractions.** Ask: "does the system already have something that solves this?" The answer is usually yes.
- **Start from the simplest possible mechanism.** Begin with minimal intervention. Add complexity only when proven insufficient.
- **No speculative code without tests.** Every code path must have a test that exercises it.
- **Stay consistent with established patterns.** When the codebase has a way of doing something, use it.
- **Never mask bugs with workaround guards.** Don't add null checks to make tests pass. Fix the root cause.
- **Pass decisions through data, don't re-derive downstream.** If a decision was made upstream, attach it to the data. Don't re-detect via fragile lookups.

## Programming Patterns

### Code style (Python)

- Functional programming style. Avoid `for` loops with mutations — use comprehensions, `map`, `filter`, `reduce`.
- Prefer early return. Use `if` for exceptional cases, not the happy path.
- Small, composable functions. No massive functions.
- Fully qualified imports. No relative imports.
- Logging, not `print` statements.
- Constants instead of magic strings and numbers.
- Enums for fixed string sets, not raw strings.

### Code style (Java)

- Follow existing patterns in the codebase.
- Prefer immutable data structures where possible.
- Use dependency injection for external systems.

### Types and values (Python)

- No defensive programming. No `None` checks, no generic exception handling. If unsure, pause and ask.
- No `None` as a default parameter. Use empty structures (`{}`, `[]`, `()`).
- No `None` returns from non-None return types. Use null object pattern.
- No mutation after construction. Inject all dependencies at construction time.

### Architecture

- Ports-and-adapters. Functional core, imperative shell.
- Dependency injection for external systems (Neo4j, OS, file I/O).

## Testing Patterns

- **TDD:** Write failing tests first. For every bug fix, write a test that fails without the fix.
- **Review assertions after writing tests.** Replace weak assertions (`assert x is not None`) with concrete value assertions (`assert result == 30`). If a concrete assertion isn't possible, document why.
- **Unit vs integration:** Unit tests (no I/O) in `tests/unit/`. Integration tests in `tests/integration/`.
- **Fixtures:** Use `pytest` fixtures and `tmp_path` for filesystem tests.
- **No mocking:** Do not use `unittest.mock.patch`. Use dependency injection with mock objects.
- **Assertions are sacred:** Do not modify test assertions unless certain the change is valid.
- **No implementation hacks for tests:** Never add special behavior just to make tests pass.

## Code Review

### Self-review checklist

Before every commit, scan the diff for these anti-patterns:

- **Workaround guards** — null checks or bare `try/except` added just to make tests pass without understanding the root cause.
- **Weak assertions** — `assert x is not None` when a concrete value assertion is possible.
- **Mutation in loops** — mutable accumulators inside `for` loops instead of comprehensions/map/filter/reduce.
- **Stale documentation** — README, ADRs, design docs that no longer match the implementation.
- **Missing tests** — new code paths without corresponding tests.
- **Leaked abstractions** — internal details exposed in public APIs or test assertions.
- **Dead code** — unused imports, unreachable branches, assigned-but-never-read variables.

### Requested reviews

When asked to review code (or when running `/review`), apply the Programming Patterns and Design Principles sections as the review rubric. Prioritise findings by severity:

1. **CRITICAL** — security vulnerabilities, data loss risks
2. **HIGH** — likely bugs, significant performance issues
3. **MEDIUM** — code quality, moderate risk
4. **LOW** — minor improvements

Report findings only. Do not fix code during review.

## Implementation Guidelines

- When the user asks to scope to a specific subdirectory or module, scope precisely. Don't run on the broader repo.
- Review subagent output for workaround guards.

## Interaction Style

- When interrupted or cancelled, immediately proceed with the new instruction. No clarifying questions — treat interruptions as implicit redirects.
- **Brainstorm collaboratively.** Present options and trade-offs to the user and actively incorporate their input before proceeding. Do not pick an approach and start implementing without discussion.
- **Stop and consult when patching.** If an implementation requires more than one corrective patch (fix-on-fix), stop. The design is wrong. Re-brainstorm the approach with the user before adding more patches.

## Python Introspection

- Write temporary scripts to `/tmp/*.py` and execute with `poetry run python /tmp/script.py`.
- Clean up temp files after use.
- Do not use `python -c` with multiline strings.

## Talisman (Secret Detection)

- If Talisman detects a potential secret, **stop** and prompt for guidance before updating `.talismanrc`.
- Don't overwrite existing `.talismanrc` entries — add at the end.

## External Dependencies

- Java modules require JDK 21+.
- `mojo-common/` and `woof/` are git submodules — run `git submodule update --init` after clone.
- Neo4j is optional (for graph persistence via `woof/`).
- Tree-sitter CLI is required for grammar generation (`npx tree-sitter`).
