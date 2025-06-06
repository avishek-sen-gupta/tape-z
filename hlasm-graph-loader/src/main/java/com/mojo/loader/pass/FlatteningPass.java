package com.mojo.loader.pass;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.RawCodeReporterVisitor;
import com.mojo.loader.navigator.SimpleTranspilerNodeTraversal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FlatteningPass {
    private final IdProvider idProvider;
    private static final Logger LOGGER = LoggerFactory.getLogger(FlatteningPass.class);

    public FlatteningPass(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public List<TranspilerInstruction> run(TranspilerNode element) {
        FlatteningVisitor visitor = new FlatteningVisitor(idProvider);
        new SimpleTranspilerNodeTraversal().traverse(element, visitor);
        LOGGER.info("==========Raw Code Report==========");
        new SimpleTranspilerNodeTraversal().traverse(element, new RawCodeReporterVisitor());
        return visitor.getFlattened();
    }
}
