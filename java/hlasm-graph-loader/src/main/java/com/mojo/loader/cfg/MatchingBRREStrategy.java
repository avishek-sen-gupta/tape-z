package com.mojo.loader.cfg;

import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.NullTranspilerInstruction;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmParserParser;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.transpiler.hlasm.AsmTranspilerNode;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MatchingBRREStrategy implements AttemptedReturnJumpStrategy {
    private static final Logger logger = LoggerFactory.getLogger(MatchingBRREStrategy.class);
    private IdProvider idProvider;

    @Override
    public TranspilerInstruction attemptedReturn(TranspilerInstruction labelStartTerminal, Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals, List<TranspilerInstruction> flattened, IdProvider idProvider) {
        this.idProvider = idProvider;
        TranspilerInstruction startTerminal = labelStartTerminal;
        int ptr = flattened.indexOf(startTerminal);
        List<String> possibleStoreREs = flattened.subList(ptr + 1, ptr + 5).stream()
                .map(this::storesRELabels)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        if (possibleStoreREs.isEmpty()) {
            logger.warn("No ST REs found for label: " + startTerminal.description());
            return new NullTranspilerInstruction(idProvider.next(), ImmutableMap.of("data", "No ST REs found within 5 instructions of entering routine"));
        }

        TranspilerInstruction brre = findBRRE(ptr, flattened, possibleStoreREs);
        if (brre instanceof NullTranspilerInstruction)
            logger.warn("WARNING, return from label not found: " + startTerminal.description());
        return brre;
    }

    private Optional<String> storesRELabels(TranspilerInstruction element) {
        if (!(element.ref() instanceof AsmTranspilerNode)) return Optional.empty();
        AsmTranspilerNode asmCodeNode = (AsmTranspilerNode) element.ref();
        Optional<HlasmParserParser.St_rule_1549Context> possibleStoreREInstruction = new HLASMInstructionNavigator().possibleStoreInstruction(asmCodeNode);
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) asmCodeNode.getInstructionTree();
        if (possibleStoreREInstruction.isEmpty()) return Optional.empty();
        HlasmParserParser.St_rule_1549Context storeREInstruction = possibleStoreREInstruction.get();
        if (storeREInstruction.operand_1_register().getText().trim().equals("RE")) {
            return Optional.of(storeREInstruction.operand_2_displacement().getText());
        }
        return Optional.empty();
    }

    private TranspilerInstruction findBRRE(int ptr, List<TranspilerInstruction> flattened, List<String> possibleStoreREs) {
        while (ptr < flattened.size()) {
            if (isBranchRegisterRE(flattened.get(ptr), possibleStoreREs, flattened.get(ptr - 1)))
                return flattened.get(ptr);
            ptr++;
        }
        return new NullTranspilerInstruction(idProvider.next(),ImmutableMap.of("data", "NULL BECAUSE NO BR RE FOUND"));
    }

    private boolean isBranchRegisterRE(TranspilerInstruction codeElement, List<String> possibleStoreREs, TranspilerInstruction previousPossibleLoadInstruction) {
        if (!(codeElement.ref() instanceof AsmTranspilerNode)) return false;
        String firstLabel = possibleStoreREs.getFirst();
        String instructionText = codeElement.originalText();
        return instructionText.contains("BR") && instructionText.contains("RE") && loadsReturnAddressIntoRE(previousPossibleLoadInstruction, firstLabel);
    }

    private boolean loadsReturnAddressIntoRE(TranspilerInstruction previousPossibleLoadInstruction, String expectedAddress) {
        if (!(previousPossibleLoadInstruction.ref() instanceof AsmTranspilerNode)) return false;
        AsmTranspilerNode asmCodeElement = (AsmTranspilerNode) previousPossibleLoadInstruction.ref();
        Optional<HlasmParserParser.L_rule_773Context> possibleLoadInstruction = new HLASMInstructionNavigator().possibleLoadInstruction(asmCodeElement);
        if (possibleLoadInstruction.isEmpty()) return false;
        HlasmParserParser.L_rule_773Context loadRule = possibleLoadInstruction.get();
        return loadRule.operand_1_register().getText().equals("RE")
                && loadRule.operand_2_displacement().getText().equals(expectedAddress);
    }
}
