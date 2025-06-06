package com.mojo.loader.symbolic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SymbolRegistry {
    private static final Logger logger = LoggerFactory.getLogger(SymbolRegistry.class);
    private final Map<String, SymbolReference> symbols = new HashMap<>();

    public void update(String symbolName, AbstractSymbol symbolValue) {
        symbols.put(symbolName, new SymbolReference(symbolName, symbolValue));
    }

    public void updateWithNamedSymbol(String source, String target) {
        if (!symbols.containsKey(source)) {
            logger.info("Symbol %s does not exist, creating it...%n", source);
            symbols.put(source, new SymbolReference(source, new ValueSymbol("NULL")));
        }

        symbols.put(target, new SymbolReference(target, symbols.get(source).resolved()));
    }

    public void log() {
        symbols.forEach((key, value) -> logger.info(value.toString()));
    }

    public SymbolReference get(String symbolName) {
        return symbols.containsKey(symbolName) ? symbols.get(symbolName) : new NullSymbolReference();
    }
}
