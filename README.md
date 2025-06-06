# HLASM Analyser

A Java-based tool for parsing and analyzing HLASM (High Level Assembler) code using ANTLR4. This project provides comprehensive tools for working with mainframe assembler code, including parsing, control flow analysis, dependency tracking, and visualization capabilities.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://adoptium.net/en-GB/temurin/releases/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-orange.svg)](https://maven.apache.org/)

## Table of Contents
- [Project Overview](#project-overview)
- [Project Structure](#project-structure)
- [Module Interactions](#module-interactions)
- [Getting Started](#getting-started)
- [Usage Examples](#usage-examples)
- [Development](#development)
- [Useful Neo4J Queries](#useful-neo4j-queries)
- [Contributing](#contributing)
- [Reporting Issues](#reporting-issues)
- [License](#license)

## Project Overview

HLASM Analyser is designed to parse, analyze, and process HLASM (High Level Assembler) code, which is commonly used in mainframe environments. The project uses ANTLR4 to define the grammar for HLASM instructions and provides tools for working with parsed HLASM code.

### Key Features

- **Comprehensive HLASM Parsing**: Parses HLASM code including labels, instructions, operands, and comments
- **Embedded SQL Support**: Recognizes and parses DB2 SQL statements embedded in HLASM code
- **Macro Expansion**: Handles macro definitions and expansions, including copybook inclusion
- **Control Flow Analysis**: Builds control flow graphs (CFG) to visualize program execution paths
- **Dependency Tracking**: Identifies and tracks dependencies between HLASM modules
- **Cyclomatic Complexity**: Calculates cyclomatic complexity metrics for code sections
- **Neo4J Integration**: Stores analysis results in Neo4J graph database for advanced querying
- **API Access**: Provides Model Context Protocol (MCP) server for programmatic access to analysis capabilities

## Project Structure

The project consists of several Maven modules that work together to provide a comprehensive solution for HLASM code analysis:

### hlasm-parser

**Purpose**: Contains the ANTLR4 grammar definitions for HLASM (including DB2 SQL embedded in HLASM) and generates the parser code.

**Key Dependencies**:
- ANTLR4 (4.13.1): For grammar definition and parser generation
- Lombok: For reducing boilerplate code

**Key Features**:
- Comprehensive grammar for HLASM instructions
- Support for DB2 SQL embedded in HLASM
- Generated lexer and parser classes for HLASM code processing

**Build Configuration**:
- Uses ANTLR4 Maven plugin to generate parser code from grammar files
- Grammar files are located in the `grammar` directory
- Generated parser code is placed in the `com.mojo.hlasm` package

### hlasm-format-loader

**Purpose**: Responsible for generating ANTLR4 grammar for HLASM based on instruction format definitions.

**Key Dependencies**:
- hlasm-parser: For the base HLASM grammar and parser
- Apache Commons CSV: For processing CSV files containing instruction format definitions
- Apache Commons Lang3: For additional utility functions
- Guava: For utility functions
- Lombok: For reducing boilerplate code

**Key Features**:
- Processes instruction format definitions
- Generates grammar rules based on instruction formats
- Enhances the base HLASM grammar with additional instruction formats

### hlasm-graph-loader

**Purpose**: Provides utilities for loading, parsing, and analyzing HLASM code, building control flow graphs, and storing them in a Neo4J database.

**Key Dependencies**:
- hlasm-format-loader: For enhanced HLASM grammar and parsing
- [mojo-common](https://github.com/asengupta/mojo-common): Common Intermediate Language support
- woof: For Neo4J database interaction
- JGraphT: For graph operations (core, ext, and io components)
- Jackson Databind: For JSON processing
- Gson: For JSON processing
- Guava: For utility functions
- Lombok: For reducing boilerplate code
- Apache Commons CSV: For CSV processing
- Apache Commons Lang3: For additional utility functions

**Key Features**:
- Parses HLASM code using the parser from hlasm-parser
- Builds control flow graphs (CFG) from parsed HLASM code
- Analyzes code structure and relationships
- Stores analysis results in Neo4J database using the woof module

**Key Classes**:
- `HlasmCodeAnalysis`: Main entry point for code analysis
- `CFGBuilder`: Builds control flow graphs from parsed HLASM code
- `HLASMTracer`: Traces execution paths through the control flow graph

### hlasm-mcp-server

**Purpose**: Implements a server component that provides API endpoints for interacting with the HLASM analysis functionality.

**Key Dependencies**:
- hlasm-graph-loader: For HLASM code analysis and CFG generation
- woof: For Neo4J database interaction
- Model Context Protocol (MCP) SDK: For API endpoint implementation
- SLF4J and Logback: For logging
- JGraphT: For graph operations
- Jackson Databind: For JSON processing
- Gson: For JSON processing
- Guava: For utility functions
- Lombok: For reducing boilerplate code

**Key Features**:
- Provides API endpoints for HLASM code analysis
- Integrates with the Model Context Protocol
- Serves as a server component for client applications

**Build Configuration**:
- Uses Maven Assembly Plugin to create a standalone JAR with dependencies
- Main class: `com.mojo.mcp.server.HlasmMCPToolServer`

### mojo-common

**Purpose**: A utility module that provides common algorithms and data structures for code analysis, particularly focused on control flow graph generation and manipulation.

**Key Dependencies**:
- woof: For graph database operations
- JGraphT: For graph data structures and algorithms
- Lombok: For reducing boilerplate code
- Apache Commons Lang3: For utility functions
- Guava: For utility functions
- Vavr: For functional programming constructs

**Key Features**:
- Provides algorithms for building and analyzing basic blocks
- Supports control flow graph generation and manipulation
- Contains common data structures for code representation
- Offers utilities for code analysis tasks

mojo-common is the same Intermediate Language library that powers [Cobol-REKT](https://github.com/avishek-sen-gupta/cobol-rekt).

### woof

**Purpose**: A utility module that serves as a graph database interface layer, specifically for Neo4J, used by other modules to store and query the analyzed HLASM code.

**Key Dependencies**:
- Neo4j Java Driver: For interacting with Neo4j graph database
- Lombok: For reducing boilerplate code
- Apache Commons Lang3: For additional utility functions
- Guava: For utility functions
- Azure AI OpenAI SDK: For AI-assisted analysis capabilities

**Key Features**:
- Provides a simplified interface for Neo4J database operations
- Handles graph data storage and retrieval
- Supports complex graph queries
- Includes utilities for working with graph data

**Key Classes**:
- `GraphSDK`: Main entry point for graph database operations
- `Neo4JDriverBuilder`: Builds Neo4J driver instances
- `WoofNode` and `WoofEdge`: Represent nodes and edges in the graph

## Module Interactions

The HLASM Analyser modules are designed to work together in a layered architecture:

1. **hlasm-parser** forms the foundation, providing the core grammar and parsing capabilities.

2. **hlasm-format-loader** extends the parser by adding support for additional instruction formats.

3. **mojo-common** provides common algorithms and data structures for code analysis, particularly for control flow graph generation and manipulation.

4. **hlasm-graph-loader** builds on the parser modules and uses mojo-common to analyze HLASM code and generate control flow graphs.

5. **hlasm-mcp-server** provides a server interface to the analysis capabilities, making them accessible via API endpoints.

6. **woof** is used by both hlasm-graph-loader and hlasm-mcp-server to store and retrieve graph data from Neo4J, and is also used by mojo-common for some graph operations.

The typical workflow is:
1. HLASM code is parsed using the grammar from hlasm-parser and hlasm-format-loader
2. The parsed code is analyzed by hlasm-graph-loader using algorithms from mojo-common to build control flow graphs
3. The analysis results are stored in Neo4J using the woof module
4. The hlasm-mcp-server provides API access to the analysis capabilities and results

## Analysis Pipeline

The HLASM Analyser processes code through a sophisticated multi-stage pipeline:

1. **File Reading**: The source HLASM file is read line by line.

2. **Line Truncation**: Lines are truncated beyond column 72, following HLASM standards.

3. **Macro Expansion**: Macros are expanded, and copybooks are included.

4. **Label Block Extraction**: Labeled blocks are identified and extracted.

5. **Line Continuation Handling**: Continued lines are collapsed into single logical lines.

6. **HLASM Parsing**: The code is parsed using the ANTLR4-generated parser.

7. **SQL Parsing**: Embedded SQL statements are identified and parsed.

8. **Macro Processing**: Both structured and unstructured macros are processed.

9. **External Call Resolution**: External calls to other modules are resolved.

10. **Dependency Tracking**: Dependencies between modules are identified and tracked.

11. **Code Flattening**: The hierarchical code structure is flattened for analysis.

12. **Control Flow Graph Generation**: A control flow graph is built from the flattened code.

13. **Cyclomatic Complexity Calculation**: Complexity metrics are calculated for code sections.

14. **Independent Component Identification**: Independent code components are identified.

This pipeline ensures comprehensive analysis of HLASM code, capturing not only the syntactic structure but also the semantic relationships and control flow.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:
- Java 21 or higher
- Maven 3.6 or higher
- Neo4J (optional, for graph storage)

### Installation

1. Clone the repository:
   ```bash
   git clone --recurse-submodules -j8 https://github.com/avishek-sen-gupta/hlasm-analyser.git
   cd hlasm-analyser
   ```

Or, if you have already cloned the repository without submodules, you can use:

```
git submodule update --init --recursive
```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Set up environment variables for Neo4J (if using):
   ```bash
   export NEO4J_URI=bolt://localhost:7687
   export NEO4J_USERNAME=neo4j
   export NEO4J_PASSWORD=your_password
   ```

4. (Optional) Install Neo4J:
   - Download from [Neo4J Download Page](https://neo4j.com/download/)
   - Follow the installation instructions for your platform
   - Start the Neo4J server before running the HLASM Analyser with Neo4J integration

## Usage Examples

### Using the Command Line Interface (CLI)

The HLASM Analyser provides a command-line interface (CLI) built with PicoCLI that offers multiple commands for analyzing and visualizing HLASM code.

#### Building the CLI

To build the CLI, run the following command from the project root:

```bash
 mvn clean install
```

This will create an executable JAR file in the `hlasm-analyser-cli/target` directory.

#### Available Commands

The CLI provides the following commands:

1. **cfg-to-json**: Exports the Control Flow Graph (CFG) to JSON
2. **flowchart**: Builds a flowchart for the entire program in one go
3. **flowchart-sections**: Builds flowcharts for all sections of the program, section by section

#### Command: cfg-to-json

This command analyzes a HLASM file and exports its control flow graph to JSON format.

**Parameters:**
- Path to the HLASM file to analyze (positional parameter)
- `-c, --copybook`: Path to the copybook directory (required)
- `-o, --output`: Path where the output JSON file will be written (required)
- `-e, --external`: Path for external programs (required)

**Example:**
```bash
java -jar hlasm-analyser-cli/target/hlasm-analyser-cli-1.0-SNAPSHOT.jar cfg-to-json /path/to/my/hlasm/file.txt -c /path/to/copybook/directory -o /path/to/output/cfg.json -e /path/to/external/programs
```

#### Command: flowchart

This command builds a flowchart visualization for the entire HLASM program.

**Parameters:**
- HLASM program name to analyze (positional parameter)
- `-s, --srcDir`: The HLASM source directory (required)
- `-cp, --copyBooksDir`: Copybook directory (required)
- `-o, --outputDir`: Output directory (required)
- `-e, --external`: Path for external programs (required)
- `-m, --model`: Foundation model to use (optional)

**Example:**

```bash
OLLAMA_ENDPOINT=http://<ollama.endpoint> java -jar hlasm-analyser-cli/target/hlasm-analyser-cli-1.0-SNAPSHOT.jar flowchart -s /path/to/source/dir -cp /path/to/copybook/dir -o /path/to/output/dir -e /path/to/external/programs -m OLLAMA program.txt
```

#### Command: flowchart-sections

This command builds flowcharts for all sections of the HLASM program, section by section.

**Parameters:**
- HLASM program name to analyze (positional parameter)
- `-s, --srcDir`: The HLASM source directory (required)
- `-cp, --copyBooksDir`: Copybook directory (required)
- `-o, --outputDir`: Output directory (required)
- `-e, --external`: Path for external programs (required)
- `-m, --model`: Foundation model to use (optional)

**Example:**

```bash
OLLAMA_ENDPOINT=http://<ollama.endpoint> java -jar hlasm-analyser-cli/target/hlasm-analyser-cli-1.0-SNAPSHOT.jar flowchart-sections -s /path/to/source/dir -cp /path/to/copybook/dir -o /path/to/output/dir -e /path/to/external/programs -m OLLAMA program.txt
```

#### CLI Help

To see all available commands and general help information:

```bash
java -jar hlasm-analyser-cli/target/hlasm-analyser-cli-1.0-SNAPSHOT.jar --help
```

To see help for a specific command:

```bash
java -jar hlasm-analyser-cli/target/hlasm-analyser-cli-1.0-SNAPSHOT.jar <command> --help
```

#### What the CLI Does

The CLI provides tools for analyzing HLASM code, building control flow graphs, and generating visualizations:

- **cfg-to-json**: Outputs a JSON representation of the control flow graph, containing nodes representing code elements and edges representing control flow between them.
- **flowchart**: Generates a visual flowchart (in DOT and SVG formats) for the entire program.
- **flowchart-sections**: Generates separate flowcharts for each section of the program, providing more detailed visualizations of specific program components.

These outputs can be used for further analysis, visualization, or integration with other tools.


## Development

### Project Dependencies

- ANTLR4 (4.13.1) for grammar definition and parser generation
- Guava for utility functions
- Lombok for reducing boilerplate code
- Apache Commons CSV for CSV processing
- Apache Commons Lang3 for additional utility functions
- JUnit 5 for testing

## Useful Neo4J queries

Identify dead code

```
MATCH (n)
WHERE NOT EXISTS {
  MATCH (m)-[r]->(n)
  WHERE type(r) <> 'FLOWS_TO_SYNTAX_ONLY'
}
RETURN n
```

Delete all nodes

```
MATCH (n) DETACH DELETE n
```

Match the whole graph
```
MATCH (n)-[r]->(d) RETURN n,r,d
```

## Contributing

Contributions to HLASM Analyser are welcome! Here's how you can contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please make sure to update tests as appropriate and follow the existing code style.

## Reporting Issues

If you encounter any bugs or have feature requests, please file an issue on the GitHub repository. When reporting issues, please include:

1. A clear and descriptive title
2. Steps to reproduce the issue
3. Expected behavior
4. Actual behavior
5. Any relevant logs or error messages
6. Your environment (OS, Java version, etc.)

## A Note on Copyright

The DB2 grammar has been graciously borrowed from the [eclipse-che4z COBOL support project](https://github.com/eclipse-che4z/che-che4z-lsp-for-cobol), and thus (together with any changes) falls under the Eclipse Public License v2.0.

The rest of the code falls under the MIT License.

## License

MIT License

Copyright (c) 2025 Avishek Sen Gupta

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
