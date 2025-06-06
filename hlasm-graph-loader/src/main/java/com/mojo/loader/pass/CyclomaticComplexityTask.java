package com.mojo.loader.pass;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.loader.analysis.CyclomaticComplexityCalculator;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CyclomaticComplexityTask {
    private static final Logger logger = LoggerFactory.getLogger(CyclomaticComplexityTask.class);

    public List<Pair<String, Long>> run(HLASMInstructionNavigator navigator, List<TranspilerInstruction> flattened) {
        Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> sectionsByLabel = navigator.labelledSections(flattened);
        List<Pair<String, List<TranspilerInstruction>>> codeChunkedByLabels = sectionsByLabel.entrySet().stream()
                .map(section -> Pair.of(section.getKey(), navigator.codeBetween(section.getValue().getLeft(), section.getValue().getRight(), flattened)))
                .toList();

        CyclomaticComplexityCalculator cyclomaticComplexityCalculator = new CyclomaticComplexityCalculator();
        List<Pair<String, Long>> complexitiesByLabel = codeChunkedByLabels.stream().map(e -> Pair.of(e.getLeft(), cyclomaticComplexityCalculator.cyclomaticComplexity(e.getRight())))
                .sorted((a, b) -> {
                    if (a.getRight().equals(b.getRight())) return 0;
                    return a.getRight() < b.getRight() ? 1 : -1;
                })
//                .filter(e -> e.getRight() > 1)
                .toList();
        logger.info(complexitiesByLabel.toString());

        return complexitiesByLabel;
    }
}
