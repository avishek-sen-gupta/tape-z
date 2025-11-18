from setuptools import setup, find_packages

setup(
    name="tapez",
    version="1.0.0",
    packages=find_packages(),
    install_requires=[
        "antlr4-python3-runtime>=4.13.1",
        "click>=8.1.7",
        "networkx>=3.2",
        "neo4j>=5.14.0",
        "graphviz>=0.20.1",
        "pydantic>=2.5.0",
        "pydantic-settings>=2.1.0",
        "requests>=2.31.0",
    ],
    entry_points={
        "console_scripts": [
            "tapez=tapez.cli.main:cli",
        ],
    },
    python_requires=">=3.11",
)
