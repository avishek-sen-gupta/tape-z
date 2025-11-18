"""CLI command implementations"""

import click
import json
import sys
from pathlib import Path
from ..graph_loader.analysis import CodeAnalyzer
from ..graph_loader.flowchart import FlowchartBuilder
from ..neo4j_integration.exporter import Neo4JExporter
from ..neo4j_integration.connection import Neo4JConnection


@click.command(name="cfg-to-json")
@click.argument("file_path", type=click.Path(exists=True))
@click.option(
    "-c",
    "--copybook",
    required=True,
    help="Path to the copybook directory",
    type=click.Path(exists=True),
)
@click.option(
    "-o",
    "--output",
    required=True,
    help="Path where the output JSON file will be written",
    type=click.Path(),
)
@click.option(
    "-e",
    "--external",
    required=True,
    help="Path for external programs",
    type=click.Path(exists=True),
)
def cfg_to_json(file_path: str, copybook: str, output: str, external: str):
    """
    Exports the Control Flow Graph (CFG) to JSON.

    Analyzes a HLASM file and exports its control flow graph to JSON format.
    """
    try:
        click.echo(f"Analyzing {file_path}...")

        analyzer = CodeAnalyzer()
        result = analyzer.analyze_file(Path(file_path))

        # Convert CFG to JSON-serializable format
        cfg_data = {
            "blocks": {
                block_id: {
                    "id": block.id,
                    "label": block.label,
                    "start_line": block.start_line,
                    "end_line": block.end_line,
                    "instructions": [
                        {
                            "mnemonic": inst.mnemonic,
                            "line_number": inst.line_number,
                            "operands": [op.value for op in inst.operands],
                        }
                        for inst in block.instructions
                    ],
                }
                for block_id, block in result.control_flow_graph.blocks.items()
            },
            "edges": [
                {
                    "from": from_node,
                    "to": to_node,
                    "type": data.get("edge_type", "FLOW"),
                }
                for from_node, to_node, data in result.control_flow_graph.graph.edges(
                    data=True
                )
            ],
            "entry_block": result.control_flow_graph.entry_block,
            "exit_blocks": list(result.control_flow_graph.exit_blocks),
            "analysis": result.to_dict(),
        }

        # Write to file
        output_path = Path(output)
        output_path.parent.mkdir(parents=True, exist_ok=True)

        with open(output_path, "w") as f:
            json.dump(cfg_data, f, indent=2)

        click.echo(f"Successfully exported CFG to {output}")
        click.echo(f"Cyclomatic Complexity: {result.cyclomatic_complexity}")
        click.echo(f"Total Blocks: {len(result.control_flow_graph.blocks)}")

    except Exception as e:
        click.echo(f"Error: {str(e)}", err=True)
        sys.exit(1)


@click.command()
@click.argument("program_name")
@click.option(
    "-s", "--srcDir", required=True, help="The HLASM source directory", type=click.Path(exists=True)
)
@click.option(
    "-cp", "--copyBooksDir", required=True, help="Copybook directory", type=click.Path(exists=True)
)
@click.option(
    "-o", "--outputDir", required=True, help="Output directory", type=click.Path()
)
@click.option(
    "-e",
    "--external",
    required=True,
    help="Path for external programs",
    type=click.Path(exists=True),
)
@click.option(
    "-m",
    "--model",
    help="Foundation model to use (optional)",
    type=str,
)
def flowchart(program_name: str, srcdir: str, copybooksdir: str, outputdir: str, external: str, model: str):
    """
    Builds a flowchart visualization for the entire HLASM program.

    Analyzes the program and creates a visual flowchart representation.
    """
    try:
        click.echo(f"Building flowchart for {program_name}...")

        # Find the program file
        src_path = Path(srcdir)
        program_file = src_path / program_name

        if not program_file.exists():
            click.echo(f"Error: Program file {program_file} not found", err=True)
            sys.exit(1)

        # Analyze the program
        analyzer = CodeAnalyzer()
        result = analyzer.analyze_file(program_file)

        # Build flowchart
        output_dir = Path(outputdir)
        output_dir.mkdir(parents=True, exist_ok=True)

        flowchart_builder = FlowchartBuilder()
        output_path = output_dir / f"flowchart_{program_name}"
        flowchart_file = flowchart_builder.build_flowchart(
            result.control_flow_graph, output_path
        )

        click.echo(f"Successfully created flowchart: {flowchart_file}")
        click.echo(f"Cyclomatic Complexity: {result.cyclomatic_complexity}")

        if model:
            click.echo(f"Note: Model {model} specified but AI summarization not yet implemented")

    except Exception as e:
        click.echo(f"Error: {str(e)}", err=True)
        sys.exit(1)


@click.command(name="flowchart-sections")
@click.argument("program_name")
@click.option(
    "-s", "--srcDir", required=True, help="The HLASM source directory", type=click.Path(exists=True)
)
@click.option(
    "-cp", "--copyBooksDir", required=True, help="Copybook directory", type=click.Path(exists=True)
)
@click.option(
    "-o", "--outputDir", required=True, help="Output directory", type=click.Path()
)
@click.option(
    "-e",
    "--external",
    required=True,
    help="Path for external programs",
    type=click.Path(exists=True),
)
@click.option(
    "-m",
    "--model",
    help="Foundation model to use (optional)",
    type=str,
)
def flowchart_sections(
    program_name: str, srcdir: str, copybooksdir: str, outputdir: str, external: str, model: str
):
    """
    Builds flowcharts for all sections of the HLASM program, section by section.

    Creates separate flowchart files for each labeled section in the program.
    """
    try:
        click.echo(f"Building section flowcharts for {program_name}...")

        # Find the program file
        src_path = Path(srcdir)
        program_file = src_path / program_name

        if not program_file.exists():
            click.echo(f"Error: Program file {program_file} not found", err=True)
            sys.exit(1)

        # Analyze the program
        analyzer = CodeAnalyzer()
        result = analyzer.analyze_file(program_file)

        # Build section flowcharts
        output_dir = Path(outputdir)
        output_dir.mkdir(parents=True, exist_ok=True)

        flowchart_builder = FlowchartBuilder()
        section_files = flowchart_builder.build_section_flowcharts(
            result.control_flow_graph, output_dir
        )

        click.echo(f"Successfully created {len(section_files)} section flowcharts:")
        for section, file_path in section_files.items():
            click.echo(f"  - {section}: {file_path}")

        if model:
            click.echo(f"Note: Model {model} specified but AI summarization not yet implemented")

    except Exception as e:
        click.echo(f"Error: {str(e)}", err=True)
        sys.exit(1)
