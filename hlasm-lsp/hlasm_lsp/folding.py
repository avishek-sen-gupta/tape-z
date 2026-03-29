from lsprotocol import types
from tree_sitter import Node
from hlasm_lsp.index import DocumentIndex


def get_folding_ranges(index: DocumentIndex) -> list[types.FoldingRange]:
    ranges: list[types.FoldingRange] = []
    for block in index.macro_blocks:
        if block.end_line > block.start_line:
            ranges.append(
                types.FoldingRange(
                    start_line=block.start_line,
                    end_line=block.end_line,
                    kind=types.FoldingRangeKind.Region,
                )
            )
    for section in index.sections:
        if section.end_line > section.start_line:
            ranges.append(
                types.FoldingRange(
                    start_line=section.start_line,
                    end_line=section.end_line,
                    kind=types.FoldingRangeKind.Region,
                )
            )
    _collect_comment_blocks(index.tree.root_node, ranges)
    return ranges


def _collect_comment_blocks(root: Node, ranges: list[types.FoldingRange]) -> None:
    comment_start: int | None = None
    comment_end: int | None = None
    for child in root.children:
        if child.type in ("comment_line", "macro_comment_line"):
            if comment_start is None:
                comment_start = child.start_point[0]
            comment_end = child.start_point[0]
        else:
            if comment_start is not None and comment_end is not None:
                if comment_end - comment_start >= 2:
                    ranges.append(
                        types.FoldingRange(
                            start_line=comment_start,
                            end_line=comment_end,
                            kind=types.FoldingRangeKind.Comment,
                        )
                    )
            comment_start = None
            comment_end = None
    if comment_start is not None and comment_end is not None:
        if comment_end - comment_start >= 2:
            ranges.append(
                types.FoldingRange(
                    start_line=comment_start,
                    end_line=comment_end,
                    kind=types.FoldingRangeKind.Comment,
                )
            )
