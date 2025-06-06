package com.mojo.loader.pipeline;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.FileMnemonicsLoader;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.LabelledCodeBlockElement;
import com.mojo.loader.pass.DiscardAfter72Pass;
import com.mojo.loader.pass.LLMSanitisePass;
import com.mojo.loader.pass.LabelBlockPass;
import com.mojo.loader.pass.MacroExpansionParsePass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractBlocksTask {
    private static final Logger logger = LoggerFactory.getLogger(ExtractBlocksTask.class);
    private final IdProvider idProvider;
    private final FileMnemonicsLoader mnemonicsLoader;

    public ExtractBlocksTask(IdProvider idProvider, FileMnemonicsLoader loader) {
        this.idProvider = idProvider;
        mnemonicsLoader = loader;
    }

    public ExtractBlocksTask(IdProvider idProvider) {
        this(idProvider, new FileMnemonicsLoader());
    }

    public List<LabelledCodeBlockElement> sections(String filePath, String copybookPath) {
        logger.info(String.format("Currently parsing: %s", filePath));
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> mnemonics = mnemonicsLoader.mnemonics();
        List<String> linesTruncatedBeyond72 = new DiscardAfter72Pass().run(lines);
        List<String> macroExpandedSource = new MacroExpansionParsePass(mnemonics, copybookPath).run(linesTruncatedBeyond72);
        List<String> llmSanitisedSource = new LLMSanitisePass().run(macroExpandedSource);
        LabelledCodeBlockElement extractedLabelledBlocksRootNode = new LabelBlockPass(idProvider).run(llmSanitisedSource);
        return extractedLabelledBlocksRootNode.getChildren().stream()
                .filter(c -> c instanceof LabelledCodeBlockElement)
                .map(c -> (LabelledCodeBlockElement) c)
                .toList();
    }
}
