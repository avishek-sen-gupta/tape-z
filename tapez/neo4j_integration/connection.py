"""Neo4J database connection management"""

import os
from typing import Optional
from neo4j import GraphDatabase, Driver
from pydantic_settings import BaseSettings


class Neo4JSettings(BaseSettings):
    """Settings for Neo4J connection."""

    uri: str = "bolt://localhost:7687"
    username: str = "neo4j"
    password: str = "password"

    class Config:
        env_prefix = "NEO4J_"


class Neo4JConnection:
    """Manages connection to Neo4J database."""

    def __init__(self, settings: Optional[Neo4JSettings] = None):
        if settings is None:
            settings = Neo4JSettings()
        self.settings = settings
        self._driver: Optional[Driver] = None

    @property
    def driver(self) -> Driver:
        """Get or create the Neo4J driver."""
        if self._driver is None:
            self._driver = GraphDatabase.driver(
                self.settings.uri,
                auth=(self.settings.username, self.settings.password),
            )
        return self._driver

    def close(self):
        """Close the Neo4J connection."""
        if self._driver is not None:
            self._driver.close()
            self._driver = None

    def verify_connectivity(self) -> bool:
        """Verify that we can connect to Neo4J."""
        try:
            self.driver.verify_connectivity()
            return True
        except Exception:
            return False

    def execute_query(self, query: str, parameters: Optional[dict] = None):
        """Execute a Cypher query."""
        with self.driver.session() as session:
            result = session.run(query, parameters or {})
            return list(result)

    def clear_database(self):
        """Clear all nodes and relationships from the database."""
        query = "MATCH (n) DETACH DELETE n"
        self.execute_query(query)

    def __enter__(self):
        """Context manager entry."""
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        """Context manager exit."""
        self.close()
