package com.mojo.visualisation;

import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.string.BasicBlockTextMaker;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.LabelledCodeBlockElement;
import com.mojo.loader.pipeline.ExtractBlocksTask;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.loader.pipeline.InMemorySourceProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildFlowchartPerSectionTask {
    public void run(UUIDProvider idProvider, String filePath, String copybookPath, String outputDir, BasicBlockTextMaker basicBlockTextMaker, String searchPath) {
        List<LabelledCodeBlockElement> sections = new ExtractBlocksTask(idProvider).sections(filePath, copybookPath);
        Map<String, List<String>> sectionsWithRawLines = sections.stream()
                .map(section -> ImmutablePair.of(section.getLabel(), lines(section)))
                .collect(Collectors.toUnmodifiableMap(ImmutablePair::getLeft, ImmutablePair::getRight));
        sectionsWithRawLines.forEach((sectionName, value) -> {
            System.out.printf("=============PROCESSING %s=============%n", sectionName);
            HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(idProvider, false).structure(new InMemorySourceProvider(sectionName, sectionsWithRawLines.get(sectionName)), copybookPath, searchPath);
            new BuildSingleFlowchartTask().run(idProvider, outputDir, basicBlockTextMaker, sectionName, analysisResult.controlFlowGraph());
        });
    }

    private static List<String> lines(LabelledCodeBlockElement section) {
        return section.getChildren().stream()
                .map(CodeElement::originalText)
                .toList();
    }
}
