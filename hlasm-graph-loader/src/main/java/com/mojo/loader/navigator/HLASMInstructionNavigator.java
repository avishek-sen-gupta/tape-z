package com.mojo.loader.navigator;

import com.mojo.algorithms.domain.*;
import com.mojo.hlasm.HlasmParserParser;
import com.mojo.transpiler.hlasm.AsmTranspilerNode;
import com.mojo.transpiler.hlasm.MacroTranspilerNode;
import com.mojo.transpiler.hlasm.SectionEndTranspilerNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mojo.graph.ConnectionType.isReachableConnectionInBasicBlock;

public class HLASMInstructionNavigator {
    private static final Logger logger = LoggerFactory.getLogger(HLASMInstructionNavigator.class);

    public static boolean isSubroutineCall(TranspilerInstruction element) {
        return isCodeOfType(element.ref(), HlasmParserParser.Bal_rule_88Context.class) ||
               isCodeOfType(element.ref(), HlasmParserParser.Balr_rule_89Context.class);
    }

    public static boolean isExit(TranspilerInstruction element) {
        return element.ref() instanceof MacroTranspilerNode && element.originalText().contains("NOLRTURN");
    }

    public boolean find(AsmTranspilerNode element, Function<TypedGraphVertex, Boolean> criteria) {
        return element.getGraph().vertexSet().stream().anyMatch(criteria::apply);
    }

    public boolean findRecursive(ParseTree ctx, Function<ParseTree, Boolean> criteria) {
        if (criteria.apply(ctx)) return true;
        if (ctx instanceof TerminalNode) return false;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (findRecursive(child, criteria)) return true;
        }
        return false;
    }


    public Optional<HlasmParserParser.St_rule_1549Context> possibleStoreInstruction(AsmTranspilerNode element) {
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) element.getInstructionTree();
        Optional<HlasmParserParser.St_rule_1549Context> storeREInstruction = Optional.ofNullable(top)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_4)
                .map(HlasmParserParser.Instruction_group_4Context::st_rule_1549);
        return storeREInstruction;
    }

    public Optional<HlasmParserParser.L_rule_773Context> possibleLoadInstruction(AsmTranspilerNode element) {
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) element.getInstructionTree();
        Optional<HlasmParserParser.L_rule_773Context> loadRule = Optional.ofNullable(top)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_2)
                .map(HlasmParserParser.Instruction_group_2Context::l_rule_773);
        return loadRule;
    }

    public Optional<HlasmParserParser.Br_rule_144Context> possibleBranchRegisterInstruction(AsmTranspilerNode element) {
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) element.getInstructionTree();
        Optional<HlasmParserParser.Br_rule_144Context> branchRegisterRule = Optional.ofNullable(top)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::br_rule_144);
        return branchRegisterRule;
    }

    public static boolean isConditionalJumpInstruction(TranspilerInstruction f) {
        return isCodeOfType(f, HlasmParserParser.Bne_rule_124Context.class)
               || isCodeOfType(f, HlasmParserParser.Be_rule_100Context.class)
               || isCodeOfType(f, HlasmParserParser.Bc_rule_93Context.class)
               || isCodeOfType(f, HlasmParserParser.Bl_rule_120Context.class)
               || isCodeOfType(f, HlasmParserParser.Bh_rule_102Context.class)
               || isCodeOfType(f, HlasmParserParser.Bc_rule_93Context.class)
               || isCodeOfType(f, HlasmParserParser.Bcr_rule_94Context.class)
               || isCodeOfType(f, HlasmParserParser.Bct_rule_95Context.class)
               || isCodeOfType(f, HlasmParserParser.Bctg_rule_96Context.class)
               || isCodeOfType(f, HlasmParserParser.Bctg_rule_97Context.class)
               || isCodeOfType(f, HlasmParserParser.Bctgr_rule_98Context.class)
               || isCodeOfType(f, HlasmParserParser.Bctr_rule_99Context.class)
               || isCodeOfType(f, HlasmParserParser.Ber_rule_101Context.class)
               || isCodeOfType(f, HlasmParserParser.Bhr_rule_103Context.class)
               || isCodeOfType(f, HlasmParserParser.Bic_rule_105Context.class)
               || isCodeOfType(f, HlasmParserParser.Bie_rule_106Context.class)
               || isCodeOfType(f, HlasmParserParser.Bih_rule_107Context.class)
               || isCodeOfType(f, HlasmParserParser.Bim_rule_109Context.class)
               || isCodeOfType(f, HlasmParserParser.Bine_rule_110Context.class)
               || isCodeOfType(f, HlasmParserParser.Binl_rule_112Context.class)
               || isCodeOfType(f, HlasmParserParser.Binm_rule_113Context.class)
               || isCodeOfType(f, HlasmParserParser.Bino_rule_114Context.class)
               || isCodeOfType(f, HlasmParserParser.Binz_rule_116Context.class)
               || isCodeOfType(f, HlasmParserParser.Biz_rule_119Context.class)
               || isCodeOfType(f, HlasmParserParser.Bl_rule_120Context.class)
               || isCodeOfType(f, HlasmParserParser.Blr_rule_121Context.class)
               || isCodeOfType(f, HlasmParserParser.Bm_rule_122Context.class)
               || isCodeOfType(f, HlasmParserParser.Bner_rule_125Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnh_rule_126Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnhr_rule_127Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnl_rule_128Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnlr_rule_129Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnm_rule_130Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnmr_rule_131Context.class)
               || isCodeOfType(f, HlasmParserParser.Bno_rule_132Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnor_rule_133Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnp_rule_134Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnpr_rule_135Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnpr_rule_135Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnz_rule_136Context.class)
               || isCodeOfType(f, HlasmParserParser.Bnzr_rule_137Context.class)
               || isCodeOfType(f, HlasmParserParser.Bo_rule_138Context.class)
               || isCodeOfType(f, HlasmParserParser.Bor_rule_139Context.class)
               || isCodeOfType(f, HlasmParserParser.Bp_rule_140Context.class)
               || isCodeOfType(f, HlasmParserParser.Bpr_rule_142Context.class)
                ;
    }

    private static boolean isCodeOfType(TranspilerInstruction f, Class<?> clazz) {
        return isCodeOfType(f.ref(), clazz);
    }

    private static boolean isCodeOfType(TranspilerNode f, Class clazz) {
        return f instanceof AsmTranspilerNode asm && isOfType(asm, clazz);
    }

    private static boolean isOfType(AsmTranspilerNode element, Class clazz) {
        return new HLASMInstructionNavigator().find(element, e -> e.type().contains(clazz.getSimpleName()));
    }

    public List<TranspilerInstruction> jumpInstructions(List<TranspilerInstruction> flattened) {
        return flattened.stream().filter(HLASMInstructionNavigator::isJumpInstruction).toList();
    }

    public List<TranspilerInstruction> conditionalumpInstructions(List<TranspilerInstruction> flattened) {
        return flattened.stream().filter(HLASMInstructionNavigator::isConditionalJumpInstruction).toList();
    }

    public Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelledSections(List<TranspilerInstruction> elements) {
        Map<String, TranspilerInstruction> labelStartTerminals = elements.stream()
                .filter(e -> e.ref() instanceof SectionStartTranspilerNode)
                .collect(Collectors.toUnmodifiableMap(TranspilerInstruction::label, e -> e));
        Map<String, TranspilerInstruction> labelEndTerminals = elements.stream()
                .filter(e -> e.ref() instanceof SectionEndTranspilerNode)
                .collect(Collectors.toUnmodifiableMap(TranspilerInstruction::label, e -> e));
        Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> startEndLabelMappings = labelStartTerminals.entrySet().stream()
                .map(e -> ImmutablePair.of(e.getKey(), ImmutablePair.of(e.getValue(), labelEndTerminals.get(e.getKey()))))
                .collect(Collectors.toUnmodifiableMap(e -> e.getLeft(), e -> e.getRight()));
        return startEndLabelMappings;
    }

    public TranspilerInstruction labelStart(String label, Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals) {
        if (!labelTerminals.containsKey(label))
            return new NullTranspilerInstruction("No label found for label " + label);
        return labelTerminals.get(label).getLeft();
    }

    public List<TranspilerInstruction> codeBetween(TranspilerInstruction startElement, TranspilerInstruction endElement, List<TranspilerInstruction> allInstructions) {
        int startIndex = allInstructions.indexOf(startElement);
        int endIndex = allInstructions.indexOf(endElement);
        return allInstructions.subList(startIndex, endIndex + 1);
    }


    public static void removeHousekeepingEdges(Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        logger.info(String.format("Before: %s...", cfg.edgeSet().size()));
        List<TypedGraphEdge> edgesToRemove = cfg.edgeSet().stream()
                .filter(e -> !isReachableConnectionInBasicBlock(e.getRelationshipType()))
                .toList();
        logger.info(String.format("Will remove %s edges...", edgesToRemove.size()));
        for (TypedGraphEdge edge : edgesToRemove) {
            cfg.removeEdge(edge);
        }

        logger.info(String.format("After: %s...", cfg.edgeSet().size()));

    }

    public static boolean isJumpInstruction(TranspilerInstruction element) {
        return element.ref() instanceof AsmTranspilerNode asm && asm.getInstructionTree().getChild(0).getText().startsWith("B");
    }

    public List<Pair<Pair<TranspilerInstruction, TranspilerInstruction>, List<TranspilerInstruction>>> findDSECT(List<TranspilerInstruction> instructions) {
        Optional<TranspilerInstruction> maybeDsectStart = instructions.stream().filter(this::isDSECT).findFirst();
        if (maybeDsectStart.isEmpty()) throw new RuntimeException("No DSECT present");
        TranspilerInstruction dsect = maybeDsectStart.get();
        int dsectIndex = instructions.indexOf(dsect);
        Collection<Pair<TranspilerInstruction, TranspilerInstruction>> values = labelledSections(instructions).values();
        List<Pair<TranspilerInstruction, TranspilerInstruction>> dsLabels = values.stream().filter(x -> instructions.indexOf(x.getLeft()) > dsectIndex).toList();
        List<Pair<Pair<TranspilerInstruction, TranspilerInstruction>, List<TranspilerInstruction>>> dataStructuresByLabels = dsLabels.stream()
                .map(d -> Pair.of(d, this.codeBetween(d.getLeft(), d.getRight(), instructions)))
                .toList();
        return dataStructuresByLabels;
    }

    private boolean isDataStructure(TranspilerInstruction element) {
        if (!(element.ref() instanceof AsmTranspilerNode)) return false;
        AsmTranspilerNode asmCodeElement = (AsmTranspilerNode) element.ref();
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) asmCodeElement.getInstructionTree();
        Optional<HlasmParserParser.Ds_rule_2680Context> maybeDS = Optional.ofNullable(top)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_7)
                .map(HlasmParserParser.Instruction_group_7Context::ds_rule_2680);
        return maybeDS.isPresent();
    }

    private boolean isDSECT(TranspilerInstruction element) {
        if (!(element.ref() instanceof AsmTranspilerNode)) return false;
        AsmTranspilerNode asmCodeElement = (AsmTranspilerNode) element.ref();
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) asmCodeElement.getInstructionTree();
        Optional<HlasmParserParser.Dsect_rule_623Context> dsectInstruction = Optional.ofNullable(top)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_2)
                .map(HlasmParserParser.Instruction_group_2Context::dsect_rule_623);
        return dsectInstruction.isPresent();
    }
}
