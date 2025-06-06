package com.mojo.loader.pipeline;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.FileMnemonicsLoader;
import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.main.IndependentComponentsPass;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pass.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HlasmCodeAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(HlasmCodeAnalysis.class);
    private final HLASMDependencyMap dependencyMap;
    private final IdProvider idProvider;
//    private final ExternalCallResolutionStrategy filePathResolutionStrategy;
    private final boolean traceTransitive;
    private final FileMnemonicsLoader mnemonicsLoader;

    public HlasmCodeAnalysis(HLASMDependencyMap dependencyMap, IdProvider idProvider, FileMnemonicsLoader loader, boolean traceTransitive) {
        this.dependencyMap = dependencyMap;
        this.idProvider = idProvider;
        this.traceTransitive = traceTransitive;
        mnemonicsLoader = loader;
    }

    public HlasmCodeAnalysis(IdProvider idProvider, boolean traceTransitive) {
        this(new HLASMDependencyMap(), idProvider, new FileMnemonicsLoader(), traceTransitive);
    }

    public HlasmCodeAnalysis(IdProvider idProvider) {
        this(idProvider, false);
    }

    public HlasmCodeAnalysisResult structure(SourceProvider srcProvider, String copybookPath, String searchPath) {
        ExternalCallResolutionStrategy filePathResolutionStrategy = new ExcludeCobolResolutionStrategy(searchPath);
        List<String> lines = srcProvider.getText();
        List<String> mnemonics = mnemonicsLoader.mnemonics();
        List<String> linesTruncatedBeyond72 = new DiscardAfter72Pass().run(lines);
        List<String> macroExpandedSource = new MacroExpansionParsePass(mnemonics, copybookPath).run(linesTruncatedBeyond72);
        List<String> llmSanitisedSource = new LLMSanitisePass().run(macroExpandedSource);
        CodeElement extractedLabelledBlocksRootNode = new LabelBlockPass(idProvider).run(llmSanitisedSource);
        CodeElement continuationCollapsedRootNode = new LineContinuationCollapsePass().run(extractedLabelledBlocksRootNode);
        CodeElement sectionFilteredRootNode = new SectionFilterPass().run(continuationCollapsedRootNode);
        CodeElement hlasmParsedRootNode = new HLASMParsePass(idProvider).run(sectionFilteredRootNode);
        CodeElement hlasmSqlParsedRootNode = new SqlParsePass(idProvider).run(hlasmParsedRootNode);
        CodeElement hlasmSqlParsedMacroSanitisedRootNode = new StructuredMacroSanitisePass(idProvider).run(hlasmSqlParsedRootNode);
        CodeElement hlasmSqlUnstructuredMacroParsedRootNode = new UnresolvedMacroParsePass(mnemonics, idProvider).run(hlasmSqlParsedMacroSanitisedRootNode);
        Pair<CodeElement, Map<String, String>> hlasmSqlUnstructuredMacroParsedRootNodeWithDeps = new ExternalCallMacroParsePass(idProvider, filePathResolutionStrategy).run(hlasmSqlUnstructuredMacroParsedRootNode);
        Map<String, String> dependencies = hlasmSqlUnstructuredMacroParsedRootNodeWithDeps.getRight();
        CodeElement hlasmSqlUnstructuredMacroParsedRootNodeWithCalls = hlasmSqlUnstructuredMacroParsedRootNodeWithDeps.getLeft();
        NodeRegistry registry = NodeRegistry.build(hlasmSqlUnstructuredMacroParsedRootNodeWithCalls);
        TranspilerNode hlasmSqlMacroParsedRootNode = new StructuredMacroParsePass(registry, idProvider).run(hlasmSqlUnstructuredMacroParsedRootNodeWithCalls);
        List<TranspilerInstruction> flattened = new FlatteningPass(idProvider).run(hlasmSqlMacroParsedRootNode);
        Pair<Graph<TranspilerInstruction, TypedGraphEdge>, Set<Pair<TranspilerInstruction, TranspilerInstruction>>> cfgBuildResult = new FullControlFlowGraphTask(idProvider).run(flattened);
        HLASMInstructionNavigator navigator = new HLASMInstructionNavigator();
        try {
            List<Pair<String, Long>> complexitiesByLabel = new CyclomaticComplexityTask().run(navigator, flattened);
        } catch (Exception e) {
            System.out.println("CyclomaticComplexity task failed, continuing with next cycle...");
        }
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = cfgBuildResult.getLeft();
        List<Pair<TranspilerInstruction, TranspilerInstruction>> independentComponents = new IndependentComponentsPass(cfgBuildResult.getRight(), flattened, cfg).run();
//        Graph<CodeElement, TypedGraphEdge> simplifiedCFG = simplifiedCFG(cfgBuildResult.getRight(), flattened, cfg);

        HlasmCodeAnalysisResult hlasmCodeAnalysisResult = new HlasmCodeAnalysisResult(cfg, ImmutableList.of(), flattened, hlasmParsedRootNode, hlasmSqlMacroParsedRootNode, cfgBuildResult.getRight(), dependencyMap);
        logger.info("Finished parsing: " + srcProvider.sourceName());
        logger.info("Deps are: " + dependencies.keySet());
        dependencyMap.put(srcProvider.sourceName(), hlasmCodeAnalysisResult);
        List<Map.Entry<String, String>> entryStream = dependencies.entrySet().stream()
                .filter(e -> !dependencyMap.containsKey(e.getValue())).toList();
        logger.info("Number of resolved dependencies are: " + dependencyMap.dependencySymbols().size());
        logger.info("Unresolved dependencies: " + entryStream);
        logger.info("Number of resolutions = " + dependencyMap.dependencySymbols().size());
        System.out.println("Number of dangling no returns=====================");
        List<TranspilerInstruction> danglingInstructions = cfg.vertexSet().stream().filter(v -> cfg.outgoingEdgesOf(v).isEmpty()).toList();
        danglingInstructions.forEach(x -> System.out.println(x.getClass()));
        List<TranspilerInstruction> orphanInstructions = cfg.vertexSet().stream().filter(v -> cfg.incomingEdgesOf(v).isEmpty()).toList();
        System.out.println("Number of orphan no returns=======================");
        orphanInstructions.forEach(x -> System.out.println(x.getClass()));

        if (!traceTransitive) return hlasmCodeAnalysisResult;
        entryStream.forEach(e -> {
            HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(dependencyMap, idProvider, new FileMnemonicsLoader(), traceTransitive).structure(e.getValue(), copybookPath, searchPath);
            dependencyMap.addCallDependency(srcProvider.sourceName(), e.getValue());
        });
        return hlasmCodeAnalysisResult;
    }

    public HlasmCodeAnalysisResult structure(String filePath, String copybookPath, String externalProgramsSearchPath) {
        return structure(new FileSourceProvider(filePath), copybookPath, externalProgramsSearchPath);
    }
}
