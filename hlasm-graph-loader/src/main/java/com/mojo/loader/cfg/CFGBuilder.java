package com.mojo.loader.cfg;

import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.*;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.algorithms.transpiler.ProgramStartLocationNode;
import com.mojo.algorithms.transpiler.ProgramTerminalLocationNode;
import com.mojo.graph.ConnectionType;
import com.mojo.hlasm.HlasmParserParser;
import com.mojo.loader.code.LabelledCodeBlockElement;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pass.MarkerInstruction;
import com.mojo.loader.transpiler.hlasm.CommentTranspilerNode;
import com.mojo.transpiler.hlasm.AsmTranspilerNode;
import com.mojo.transpiler.hlasm.SectionEndTranspilerNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static com.mojo.graph.ConnectionType.RETURNS_FROM_PROC;

public class CFGBuilder {
    private static final Logger logger = LoggerFactory.getLogger(CFGBuilder.class);
    private final HLASMInstructionNavigator navigator = new HLASMInstructionNavigator();
    private final Graph<TranspilerInstruction, TypedGraphEdge> graph = new DirectedPseudograph<>(TypedGraphEdge.class);
    private final Set<Pair<TranspilerInstruction, TranspilerInstruction>> possibleSubroutines = new HashSet<>();
    private final TranspilerInstruction EXIT;
    private final TranspilerInstruction ENTER;
    private final IdProvider idProvider;

    public CFGBuilder(IdProvider idProvider) {
        this.idProvider = idProvider;
        ENTER = new MarkerInstruction(ProgramStartLocationNode.START, CodeSentinelType.ENTER, idProvider.next());
        EXIT = new MarkerInstruction(ProgramTerminalLocationNode.END, CodeSentinelType.EXIT, idProvider.next());
    }

    public Pair<Graph<TranspilerInstruction, TypedGraphEdge>, Set<Pair<TranspilerInstruction, TranspilerInstruction>>> run(List<TranspilerInstruction> flattenedWithComments) {
        List<TranspilerInstruction> flattened = flattenedWithComments.stream().filter(l -> !(l.ref() instanceof CommentTranspilerNode)).toList();

        graph.addVertex(ENTER);
        graph.addVertex(EXIT);
//        List<TranspilerInstruction> flattened = flattenedWithComments;
        flattened.forEach(graph::addVertex);
        TranspilerInstruction previous = ENTER;

        for (TranspilerInstruction current : flattened) {
            if (current instanceof MarkerInstruction m
                && m.sentinel() == CodeSentinelType.ENTER
                && m.ref() instanceof IfTranspilerNode) {
                TranspilerInstruction endOfIfBlock = endOfIf(flattened, m);
                TranspilerInstruction startOfIfBody = ifBodyTerminal(flattened, m, CodeSentinelType.ENTER);
                TranspilerInstruction endOfIfBody = ifBodyTerminal(flattened, m, CodeSentinelType.EXIT);
                TranspilerInstruction startOfElse = elseTerminal(flattened, m, CodeSentinelType.ENTER);
                TranspilerInstruction endOfElse = elseTerminal(flattened, m, CodeSentinelType.EXIT);

                if (endOfIfBlock instanceof NullTranspilerInstruction ||
                    startOfIfBody instanceof NullTranspilerInstruction ||
                    endOfIfBody instanceof NullTranspilerInstruction ||
                    startOfElse instanceof NullTranspilerInstruction ||
                    endOfElse instanceof NullTranspilerInstruction) {
                    System.out.println("Something went wrong when trying to run CFGBuilder");
                    throw new RuntimeException("Something went wrong when trying to run CFGBuilder");
                }
                connectGraph(current, startOfIfBody, ConnectionType.FLOWS_TO_IF_TRUE);
                connectGraph(current, startOfElse, ConnectionType.FLOWS_TO_IF_FALSE);
                connectGraph(endOfIfBody, endOfIfBlock, ConnectionType.FLOWS_TO);
                connectGraph(endOfElse, endOfIfBlock, ConnectionType.FLOWS_TO);
            }

            if (HLASMInstructionNavigator.isExit(current))
                connectGraph(current, EXIT, ConnectionType.FLOWS_TO);
            if (previous instanceof MarkerInstruction m
                && (isEndOfElseBlock(m) || isEndOfThenBlock(m))) {
                connectGraphSyntactically(previous, current);
            } else if (HLASMInstructionNavigator.isConditionalJumpInstruction(previous)) {
                connectGraphIfFalse(previous, current);
            } else if (HLASMInstructionNavigator.isSubroutineCall(previous)) {
                connectWeaklyGraph(previous, current);
            } else if (HLASMInstructionNavigator.isJumpInstruction(previous)) {
                connectGraphSyntactically(previous, current);
            } else if (HLASMInstructionNavigator.isExit(previous)) {
                connectGraph(previous, EXIT, ConnectionType.FLOWS_TO);
                // Don't connect, this is a specific pattern
            } else {
                connectGraph(previous, current, ConnectionType.FLOWS_TO);
            }
            previous = current;
        }

        connectJumpInstructions(flattened, graph);
        logger.info(String.format("Created %s nodes and %s edges...", graph.vertexSet().size(), graph.edgeSet().size()));
        return ImmutablePair.of(epilogue(graph, flattened), possibleSubroutines);
    }

    private Graph<TranspilerInstruction, TypedGraphEdge> epilogue(Graph<TranspilerInstruction, TypedGraphEdge> graph, List<TranspilerInstruction> flattened) {
        Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> chunkedSections = new HLASMInstructionNavigator().labelledSections(flattened);
        Pair<TranspilerInstruction, TranspilerInstruction> returnBlock = chunkedSections.get("RETURN");
        if (returnBlock == null) return graph;
        possibleSubroutines.add(ImmutablePair.of(returnBlock.getLeft(), returnBlock.getRight()));
        return graph;
    }

    private static boolean isEndOfElseBlock(MarkerInstruction previous) {
        return previous.sentinel() == CodeSentinelType.EXIT
               && (previous.categories().contains(SemanticCategory.IF_ELSE_BODY));
    }

    private static boolean isEndOfThenBlock(MarkerInstruction previous) {
        return previous.sentinel() == CodeSentinelType.EXIT
               && (previous.categories().contains(SemanticCategory.IF_THEN_BODY));
    }

    private void connectJumpInstructions(List<TranspilerInstruction> flattened, Graph<TranspilerInstruction, TypedGraphEdge> graph) {
        List<TranspilerInstruction> jumps = navigator.jumpInstructions(flattened);
        Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals = navigator.labelledSections(flattened);
        jumps.forEach(jump -> {
            logger.debug("Jump is " + jump.originalText());
            TranspilerInstruction returnDestination = flattened.get(flattened.indexOf(jump) + 1);
            TranspilerInstruction dest = destinationLabel(jump, labelTerminals);
            if (dest instanceof NullTranspilerInstruction) {
                if (!isJumpToSubroutine(jump)) {
                    logger.warn("Couldnt find a label with name: " + jump.originalText());
                    return;
                }
                // Cheat, make jump to subroutine flow to next instruction, simulating continuity
                graph.addEdge(jump, returnDestination, new TypedGraphEdge(ConnectionType.FLOWS_TO.value(), "NONAME"));
                return;
            }
            logger.debug("FOUND LABEL: " + dest.originalText() + " for  jump: " + jump.originalText());
            graph.addEdge(jump, dest, forwardEdge(jump));

            TranspilerInstruction returnSite = attemptedReturn(dest, labelTerminals, flattened);

            if (returnSite instanceof NullTranspilerInstruction) {
                logger.warn("Dangling end of section without BR RE");
                return;
            }
            logger.debug("Return site identified as: " + returnSite.originalText());
            possibleSubroutines.add(ImmutablePair.of(dest, returnSite));
            graph.addEdge(returnSite, returnDestination, new TypedGraphEdge(RETURNS_FROM_PROC.value(), "NONAME"));
        });
    }

    private TypedGraphEdge forwardEdge(TranspilerInstruction jump) {
        boolean isSubroutine = isJumpToSubroutine(jump);
        boolean isConditionalJump = HLASMInstructionNavigator.isConditionalJumpInstruction(jump);
        if (isSubroutine)
            return new TypedGraphEdge(ConnectionType.CALLS_PROC.value(), "NONAME");
        else if (isConditionalJump)
            return new TypedGraphEdge(ConnectionType.FLOWS_TO_IF_TRUE.value(), "NONAME");
        return new TypedGraphEdge(ConnectionType.FLOWS_TO.value(), "NONAME");
    }

    private boolean isJumpToSubroutine(TranspilerInstruction jump) {
        if (!(jump.ref() instanceof AsmTranspilerNode asm)) return false;
        return branchAndLinkSearch((HlasmParserParser.StartRuleContext) asm.getInstructionTree()) != null;
    }

    // TODO: Encode return metadata to find return path more easily when tracing
    private TranspilerInstruction attemptedReturn(TranspilerInstruction labelStartTerminal, Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals, List<TranspilerInstruction> flattened) {
        return new MatchingBRREStrategy().attemptedReturn(labelStartTerminal, labelTerminals, flattened, idProvider);
    }

    private TranspilerInstruction destinationLabel(TranspilerInstruction jump, Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals) {
        AsmTranspilerNode someJump = (AsmTranspilerNode) jump.ref();
        ParseTree instructionTree = someJump.getInstructionTree();
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) instructionTree;
        String label = Stream.of(unconditionalBranchSearch(top),
                        branchAndLinkSearch(top), unconditionalBranchToRegisterAddressSearch(top),
                        branchIfEqualSearch(top), branchIfNotEqualSearch(top),
                        branchLowSearch(top), branchHighSearch(top),
                        branchNotHigh(top), branchNotPositive(top),
                        branchOnCount(top), branchNotLow(top),
                        branchNotZero(top), branchOnMinusSearch(top),
                        branchOnZeroSearch(top), branchOnCountRelativeSearch(top),
                        branchOnOverflowSearch(top), branchOnNotOverflowSearch(top))
                .reduce(null, (a, b) -> b != null ? b : a);
        TranspilerInstruction resolvedLabelStartTerminal = navigator.labelStart(label, labelTerminals);
        return resolvedLabelStartTerminal != null ? resolvedLabelStartTerminal : new NullTranspilerInstruction(idProvider.next(), ImmutableMap.of("data", "No destination label for " + jump.originalText() + " found"));
    }

    // Doesn't check for BALR yet
    private String branchAndLinkSearch(HlasmParserParser.StartRuleContext instructionTree) {
        String balDestinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bal_rule_88)
                .map(HlasmParserParser.Bal_rule_88Context::operand_2_displacement)
                .map(HlasmParserParser.Operand_2_displacementContext::getText)
                .orElse(null);
        String balrDestinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::balr_rule_89)
                .map(HlasmParserParser.Balr_rule_89Context::operand_2_register)
                .map(HlasmParserParser.Operand_2_registerContext::getText)
                .orElse(null);
        return balDestinationIdentifier != null ? balDestinationIdentifier : balrDestinationIdentifier;
    }


    private static String unconditionalBranchSearch(HlasmParserParser.StartRuleContext instructionTree) {
        String destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::b_rule_86)
                .map(HlasmParserParser.B_rule_86Context::operand_1_displacement)
                .map(HlasmParserParser.Operand_1_displacementContext::getText)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A B RULE: " + destinationIdentifier);
            return destinationIdentifier;
        }
        return null;
    }

    private static String branchIfNotEqualSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bne_rule_124)
                .map(HlasmParserParser.Bne_rule_124Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BNE RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchOnMinusSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bm_rule_122)
                .map(HlasmParserParser.Bm_rule_122Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BM RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchLowSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bl_rule_120)
                .map(HlasmParserParser.Bl_rule_120Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BL RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchNotHigh(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bnh_rule_126)
                .map(HlasmParserParser.Bnh_rule_126Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BNH RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchOnCount(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bct_rule_95)
                .map(HlasmParserParser.Bct_rule_95Context::operand_2_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BCT RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchOnCountRelativeSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bctr_rule_99)
                .map(HlasmParserParser.Bctr_rule_99Context::operand_2_register)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BCTR RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchOnOverflowSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bo_rule_138)
                .map(HlasmParserParser.Bo_rule_138Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BO RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchOnNotOverflowSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bno_rule_132)
                .map(HlasmParserParser.Bno_rule_132Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BNO RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchNotZero(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bnz_rule_136)
                .map(HlasmParserParser.Bnz_rule_136Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BNZ RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchOnZeroSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bz_rule_195)
                .map(HlasmParserParser.Bz_rule_195Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BZ RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchNotLow(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bnl_rule_128)
                .map(HlasmParserParser.Bnl_rule_128Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BNL RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchNotPositive(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bnp_rule_134)
                .map(HlasmParserParser.Bnp_rule_134Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BNP RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchHighSearch(HlasmParserParser.StartRuleContext instructionTree) {
        ParseTree destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bh_rule_102)
                .map(HlasmParserParser.Bh_rule_102Context::operand_1_displacement)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BH RULE: " + destinationIdentifier.getText());
            return destinationIdentifier.getText();
        }
        return null;
    }

    private static String branchIfEqualSearch(HlasmParserParser.StartRuleContext instructionTree) {
        String destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::be_rule_100)
                .map(HlasmParserParser.Be_rule_100Context::operand_1_displacement)
                .map(HlasmParserParser.Operand_1_displacementContext::getText)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BE RULE: " + destinationIdentifier);
            return destinationIdentifier;
        }
        return null;
    }

    private static String unconditionalBranchToRegisterAddressSearch(HlasmParserParser.StartRuleContext instructionTree) {
        String destinationIdentifier = Optional.ofNullable(instructionTree)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::br_rule_144)
                .map(HlasmParserParser.Br_rule_144Context::operand_1_register)
                .map(HlasmParserParser.Operand_1_registerContext::getText)
                .orElse(null);
        if (destinationIdentifier != null) {
            logger.debug("FOUND A BR RULE: " + destinationIdentifier);
            return destinationIdentifier;
        }
        return null;
    }

    private void connectWeaklyGraph(TranspilerInstruction from, TranspilerInstruction to) {
        connectGraph(from, to, ConnectionType.FLOWS_AFTER_SUBROUTINE);
    }

    private void connectGraphIfFalse(TranspilerInstruction from, TranspilerInstruction to) {
        connectGraph(from, to, ConnectionType.FLOWS_TO_IF_FALSE);
    }

    private void connectGraphSyntactically(TranspilerInstruction from, TranspilerInstruction to) {
        connectGraph(from, to, ConnectionType.FLOWS_TO_SYNTAX_ONLY);
    }

    private void connectGraph(TranspilerInstruction from, TranspilerInstruction to, ConnectionType relationshipType) {
        if (from instanceof NullTranspilerInstruction || to instanceof NullTranspilerInstruction) return;
        if (graph.getEdge(from, to) != null) return;
        graph.addEdge(from, to, new TypedGraphEdge(relationshipType.value(), "NONAME"));
    }

    private TranspilerInstruction endOfIf(List<TranspilerInstruction> flattened, MarkerInstruction element) {
        return flattened.stream()
                .filter(f -> f instanceof MarkerInstruction m
                             && m.sentinel() == CodeSentinelType.EXIT
                             && m.ref().id().equals(element.ref().id()))
                .findFirst().orElse(new NullTranspilerInstruction("ABCD"));
    }

    private TranspilerInstruction elseTerminal(List<TranspilerInstruction> flattened, MarkerInstruction element, CodeSentinelType terminalType) {
        IfTranspilerNode ifNode = (IfTranspilerNode) element.ref();
        return flattened.stream().filter(f -> f instanceof MarkerInstruction m
                                              && m.sentinel() == terminalType
                                              && (m.ref().id().equals(ifNode.getIfElseBlock().id())))
                .findFirst().orElse(new NullTranspilerInstruction("ABCD"));
    }

    private TranspilerInstruction ifBodyTerminal(List<TranspilerInstruction> flattened, MarkerInstruction element, CodeSentinelType terminalType) {
        IfTranspilerNode ifNode = (IfTranspilerNode) element.ref();
        return flattened.stream().filter(f -> f instanceof MarkerInstruction m
                                              && m.sentinel() == terminalType
                                              && (m.ref().id().equals(ifNode.getIfThenBlock().id())))
                .findFirst().orElse(new NullTranspilerInstruction("ABCD"));
    }
}
