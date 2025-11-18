"""Main CLI entry point for Tape/Z"""

import click
import sys
from pathlib import Path
from .commands import cfg_to_json, flowchart, flowchart_sections


@click.group()
@click.version_option(version="1.0.0")
def cli():
    """
    Tape/Z - Tools for Assembly Program Exploration for Z/OS (Python Edition)

    A toolkit for analyzing mainframe HLASM code, including parsing,
    control flow graph building, and dependency tracing.
    """
    pass


# Register commands
cli.add_command(cfg_to_json)
cli.add_command(flowchart)
cli.add_command(flowchart_sections)


if __name__ == "__main__":
    cli()
