"""UUID generation utilities"""

import uuid


class UUIDProvider:
    """Provides unique identifiers for graph nodes and other entities."""

    @staticmethod
    def generate() -> str:
        """Generate a new UUID as a string."""
        return str(uuid.uuid4())

    @staticmethod
    def generate_with_prefix(prefix: str) -> str:
        """Generate a UUID with a prefix."""
        return f"{prefix}_{uuid.uuid4()}"
