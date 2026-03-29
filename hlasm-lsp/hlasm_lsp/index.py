from __future__ import annotations

from dataclasses import dataclass, field
from tree_sitter import Tree, Node


SECTION_OPS = {"CSECT", "DSECT", "RSECT"}
MACRO_START_OPS = {"MACRO"}
MACRO_END_OPS = {"MEND"}


@dataclass(frozen=True)
class SymbolLocation:
    uri: str
    line: int
    character: int
    end_character: int


@dataclass(frozen=True)
class SymbolAtPosition:
    name: str
    node: Node
    line: int
    character: int


@dataclass(frozen=True)
class SectionInfo:
    name: str
    kind: str
    start_line: int
    end_line: int


@dataclass(frozen=True)
class MacroBlock:
    start_line: int
    end_line: int


@dataclass
class DocumentIndex:
    uri: str
    tree: Tree
    definitions: dict[str, list[SymbolLocation]] = field(default_factory=dict)
    references: dict[str, list[SymbolLocation]] = field(default_factory=dict)
    sections: list[SectionInfo] = field(default_factory=list)
    macro_blocks: list[MacroBlock] = field(default_factory=list)
    equ_values: dict[str, str] = field(default_factory=dict)

    @staticmethod
    def from_tree(tree: Tree, uri: str) -> DocumentIndex:
        index = DocumentIndex(uri=uri, tree=tree)
        _walk_tree(tree.root_node, index)
        _close_open_sections(tree.root_node, index)
        return index

    def get_definition(self, name: str) -> list[SymbolLocation]:
        return self.definitions.get(name.upper(), self.definitions.get(name, []))

    def get_references(self, name: str) -> list[SymbolLocation]:
        return self.references.get(name.upper(), self.references.get(name, []))

    def get_symbol_at(self, line: int, character: int) -> SymbolAtPosition | None:
        node = _find_named_node_at(self.tree.root_node, line, character)
        if node is None:
            return None
        return SymbolAtPosition(
            name=node.text.decode("utf-8"),
            node=node,
            line=node.start_point[0],
            character=node.start_point[1],
        )


def _add_to_map(mapping: dict[str, list[SymbolLocation]], name: str, loc: SymbolLocation) -> None:
    if name not in mapping:
        mapping[name] = []
    mapping[name].append(loc)


def _node_text(node: Node) -> str:
    return node.text.decode("utf-8")


def _make_loc(node: Node, uri: str) -> SymbolLocation:
    return SymbolLocation(
        uri=uri,
        line=node.start_point[0],
        character=node.start_point[1],
        end_character=node.end_point[1],
    )


def _walk_tree(node: Node, index: DocumentIndex) -> None:
    if node.type == "instruction_statement":
        _process_instruction(node, index)
        return
    for child in node.children:
        _walk_tree(child, index)


def _process_instruction(node: Node, index: DocumentIndex) -> None:
    label_node = node.child_by_field_name("label")
    operation_node = node.child_by_field_name("operation")
    operands_node = node.child_by_field_name("operands")

    op_text = ""
    if operation_node is not None:
        op_text = _node_text(operation_node).upper()

    # Handle GLR ambiguity: when tree-sitter resolves [label operation] as
    # [operation operands] and operands is a section keyword, treat as
    # label=operation_text, operation=operands_text.
    # e.g. "MYPROG   CSECT" may parse as operation=MYPROG, operands=CSECT
    effective_label_node = label_node
    effective_op_text = op_text
    effective_operands_node = operands_node
    if (
        label_node is None
        and operands_node is not None
        and _node_text(operands_node).upper() in SECTION_OPS | {"EQU", "MACRO", "MEND"}
    ):
        # operation is actually the label; operands is the actual operation
        effective_label_node = operation_node
        effective_op_text = _node_text(operands_node).upper()
        effective_operands_node = None

    if effective_label_node is not None:
        label_sym = _extract_symbol_name(effective_label_node)
        if label_sym:
            _add_to_map(index.definitions, label_sym, _make_loc(effective_label_node, index.uri))

    if effective_op_text in SECTION_OPS and effective_label_node is not None:
        label_sym = _extract_symbol_name(effective_label_node)
        if label_sym:
            index.sections.append(
                SectionInfo(name=label_sym, kind=effective_op_text, start_line=node.start_point[0], end_line=node.end_point[0])
            )

    if effective_op_text == "EQU" and effective_operands_node is not None and effective_label_node is not None:
        label_sym = _extract_symbol_name(effective_label_node)
        if label_sym:
            index.equ_values[label_sym] = _node_text(effective_operands_node).strip()

    if effective_op_text in MACRO_START_OPS:
        index.macro_blocks.append(MacroBlock(start_line=node.start_point[0], end_line=-1))
    if effective_op_text in MACRO_END_OPS and index.macro_blocks:
        last = index.macro_blocks[-1]
        if last.end_line == -1:
            index.macro_blocks[-1] = MacroBlock(start_line=last.start_line, end_line=node.start_point[0])

    if effective_operands_node is not None:
        _collect_references(effective_operands_node, index)


def _extract_symbol_name(node: Node) -> str:
    if node.type == "symbol":
        return _node_text(node)
    for child in node.children:
        if child.type == "symbol":
            return _node_text(child)
    return _node_text(node)


def _collect_references(node: Node, index: DocumentIndex) -> None:
    if node.type == "symbol":
        name = _node_text(node)
        _add_to_map(index.references, name, _make_loc(node, index.uri))
        return
    if node.type == "variable_symbol":
        name = _node_text(node)
        _add_to_map(index.references, name, _make_loc(node, index.uri))
        return
    for child in node.children:
        _collect_references(child, index)


def _close_open_sections(root: Node, index: DocumentIndex) -> None:
    if not index.sections:
        return
    last_line = root.end_point[0]
    for i, section in enumerate(index.sections):
        end = last_line
        if i + 1 < len(index.sections):
            end = index.sections[i + 1].start_line - 1
        index.sections[i] = SectionInfo(name=section.name, kind=section.kind, start_line=section.start_line, end_line=end)


def _find_named_node_at(node: Node, line: int, character: int) -> Node | None:
    if node.type == "symbol" or node.type == "variable_symbol":
        start_row, start_col = node.start_point
        end_row, end_col = node.end_point
        if start_row == line and start_col <= character < end_col:
            return node
    for child in node.children:
        result = _find_named_node_at(child, line, character)
        if result is not None:
            return result
    return None
