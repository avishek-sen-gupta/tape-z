package com.mojo.loader.pass;

import java.util.Optional;

public interface ExternalCallResolutionStrategy {
    Optional<String> run(String externalSymbol);
}
