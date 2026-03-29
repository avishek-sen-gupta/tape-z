from lsprotocol import types
from tree_sitter import Tree, Node


def extract_diagnostics(tree: Tree) -> list[types.Diagnostic]:
    diagnostics: list[types.Diagnostic] = []
    _collect_errors(tree.root_node, diagnostics)
    return diagnostics


def _collect_errors(node: Node, diagnostics: list[types.Diagnostic]) -> None:
    if node.type == "ERROR":
        diagnostics.append(
            types.Diagnostic(
                range=types.Range(
                    start=types.Position(line=node.start_point[0], character=node.start_point[1]),
                    end=types.Position(line=node.end_point[0], character=node.end_point[1]),
                ),
                severity=types.DiagnosticSeverity.Error,
                source="hlasm-lsp",
                message=f"Syntax error at line {node.start_point[0] + 1}, column {node.start_point[1] + 1}",
            )
        )
    elif node.is_missing:
        diagnostics.append(
            types.Diagnostic(
                range=types.Range(
                    start=types.Position(line=node.start_point[0], character=node.start_point[1]),
                    end=types.Position(line=node.end_point[0], character=node.end_point[1]),
                ),
                severity=types.DiagnosticSeverity.Error,
                source="hlasm-lsp",
                message=f"Expected '{node.type}' at line {node.start_point[0] + 1}",
            )
        )

    for child in node.children:
        _collect_errors(child, diagnostics)
