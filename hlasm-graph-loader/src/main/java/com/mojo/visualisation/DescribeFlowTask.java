package com.mojo.visualisation;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.CodeSentinelType;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.loader.pass.MarkerInstruction;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.transpiler.hlasm.SectionEndTranspilerNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jgrapht.Graph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DescribeFlowTask {
    public String run(Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph, HlasmCodeAnalysisResult analysisResult) {
        Map<TranspilerInstruction, BasicBlock<SummarisedBasicBlock>> instructionToBasicBlockMap = summarisedBlockGraph.vertexSet().stream()
                .flatMap(bb ->
                        bb.firstInstruction().bb().getInstructions().stream()
                                .map(instr -> ImmutablePair.of(instr, bb)))
                .collect(Collectors.toUnmodifiableMap(ImmutablePair::getLeft, ImmutablePair::getRight));
        System.out.println("COOL");
        Map<BasicBlock<SummarisedBasicBlock>, Boolean> describedMap = summarisedBlockGraph.vertexSet().stream()
                .collect(Collectors.toMap(v -> v, v -> false));
        List<TranspilerInstruction> flattened = analysisResult.flattened();
        List<String> list = flattened.stream().map(instruction -> textFlow(instruction, instructionToBasicBlockMap, describedMap)).toList();
        String finalFlowSummary = list.stream().reduce("", (fullSummary, summaryLine) -> summaryLine.isEmpty() ? fullSummary : fullSummary + summaryLine + "\n");
        return finalFlowSummary;
    }

    private String textFlow(TranspilerInstruction instruction, Map<TranspilerInstruction, BasicBlock<SummarisedBasicBlock>> instructionToBasicBlockMap, Map<BasicBlock<SummarisedBasicBlock>, Boolean> describedMap) {
        if (!instructionToBasicBlockMap.containsKey(instruction)) return instruction.originalText();
        BasicBlock<SummarisedBasicBlock> summarisedBasicBlock = instructionToBasicBlockMap.get(instruction);
        if (describedMap.get(summarisedBasicBlock)) return "";
        describedMap.put(summarisedBasicBlock, true);

        BasicBlock<TranspilerInstruction> realBasicBlock = summarisedBasicBlock.firstInstruction().bb();
        if (realBasicBlock.size() == 1 && realBasicBlock.firstInstruction() instanceof MarkerInstruction mi) {
            if (mi.ref() instanceof SectionStartTranspilerNode) return mi.originalText();
            else if (mi.ref() instanceof SectionEndTranspilerNode) return mi.originalText();
            else if (mi.ref() instanceof IfTranspilerNode && mi.sentinel() == CodeSentinelType.EXIT)
                return mi.originalText();
        } else if (realBasicBlock.firstInstruction() instanceof MarkerInstruction mi && mi.ref() instanceof TranspilerCodeBlockNode cb) {
            return cb.getCategories().toString() + summarisedBasicBlock.firstInstruction().label();
        }
        return summarisedBasicBlock.firstInstruction().label();
    }
}
