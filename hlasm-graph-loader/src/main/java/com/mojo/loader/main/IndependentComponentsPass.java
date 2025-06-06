package com.mojo.loader.main;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pass.MarkerInstruction;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.mojo.graph.ConnectionType.isReachableConnection;

public class IndependentComponentsPass {
    private static final Logger logger = LoggerFactory.getLogger(IndependentComponentsPass.class);

    private final Set<Pair<TranspilerInstruction, TranspilerInstruction>> possibleSubroutines;
    private final List<TranspilerInstruction> flattened;
    private final List<Pair<TranspilerInstruction, TranspilerInstruction>> verifiedSubroutines = new ArrayList<>();
    private final Graph<TranspilerInstruction, TypedGraphEdge> cfg;

    public IndependentComponentsPass(Set<Pair<TranspilerInstruction, TranspilerInstruction>> possibleSubroutines, List<TranspilerInstruction> flattened, Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        this.possibleSubroutines = possibleSubroutines;
        this.flattened = flattened;
        this.cfg = cfg;
    }

    public List<Pair<TranspilerInstruction, TranspilerInstruction>> run() {
        List<Pair<TranspilerInstruction, TranspilerInstruction>> validSubroutinesThisPass;
        int pass = 1;
        do {
            logger.info(String.format("=======Pass %s=======", pass));
            validSubroutinesThisPass = possibleSubroutines.stream()
                    .map(this::check).flatMap(Collection::stream)
                    .toList();
            verifiedSubroutines.addAll(validSubroutinesThisPass);
            for (Pair<TranspilerInstruction, TranspilerInstruction> validRoutine : validSubroutinesThisPass) {
                possibleSubroutines.remove(validRoutine);
            }
            pass++;
        } while (!validSubroutinesThisPass.isEmpty());

        logger.info("====VERIFIED SUBROUTINES====");
        verifiedSubroutines.forEach(vsub -> logger.info(vsub.getLeft().ref().label()));
        return verifiedSubroutines;
    }

    private List<Pair<TranspilerInstruction, TranspilerInstruction>> check(Pair<TranspilerInstruction, TranspilerInstruction> subroutine) {
        logger.debug("Checking subroutine " + subroutine.getLeft().originalText());
        int startIndex = flattened.indexOf(subroutine.getLeft());
        int endIndex = flattened.indexOf(subroutine.getRight());

        List<TranspilerInstruction> segment = flattened.subList(startIndex, endIndex); // Exclude BR RE
        List<TranspilerInstruction> misbehavingBranches = segment.stream().filter(HLASMInstructionNavigator::isJumpInstruction)
                .filter(element -> !jumpsInsideSegment(element, startIndex, endIndex))
                .filter(element -> !jumpsToAnotherSubroutine(element))
                .filter(element -> !jumpsToExit(element))
                .toList();
        if (misbehavingBranches.isEmpty()) {
            logger.debug("No misbehaving branches found for subroutine: " + subroutine.getLeft().originalText());
            return ImmutableList.of(subroutine);
        } else {
            logger.warn("Found misbehaving branches: " + subroutine.getLeft().ref().label());
            return ImmutableList.of();
        }
    }

    private boolean jumpsToExit(TranspilerInstruction element) {
        List<TypedGraphEdge> outgoing = cfg.outgoingEdgesOf(element).stream()
                .filter(edge -> isReachableConnection(edge.getRelationshipType()))
                .toList();
        if (outgoing.size() > 1) return false;
        TranspilerInstruction edgeTarget = cfg.getEdgeTarget(outgoing.getFirst());
        return edgeTarget instanceof MarkerInstruction mi && "RETURN".equals(mi.label());
    }

    private boolean jumpsToAnotherSubroutine(TranspilerInstruction element) {
        return HLASMInstructionNavigator.isSubroutineCall(element) && jumpsToValidSubroutine(element);
    }

    private boolean jumpsToValidSubroutine(TranspilerInstruction element) {
        return verifiedSubroutines.stream()
                .map(sub -> element.originalText().contains(sub.getLeft().label()))
                .reduce(false, (a, b) -> a || b);
    }

    private boolean jumpsInsideSegment(TranspilerInstruction element, int startIndex, int endIndex) {
        if (HLASMInstructionNavigator.isSubroutineCall(element))
            return true; // Not considering BAL/BALR instructions in this check
        List<Integer> branchTargetIndices = cfg.outgoingEdgesOf(element).stream()
                .map(cfg::getEdgeTarget)
                .map(flattened::indexOf)
                .toList();
        return branchTargetIndices.stream()
                .map(bti -> bti >= startIndex && bti <= endIndex)
                .reduce(true, (a, b) -> a && b);
    }
}
