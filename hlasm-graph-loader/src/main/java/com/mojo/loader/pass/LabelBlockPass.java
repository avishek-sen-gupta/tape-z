package com.mojo.loader.pass;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.ExecSqlPatternMatcher;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.CommentElement;
import com.mojo.loader.code.LabelledCodeBlockElement;
import com.mojo.loader.code.RawCodeElement;

import java.util.List;

public class LabelBlockPass {
    private final IdProvider idProvider;
    private CodeElement currentBlock;
    private final LabelledCodeBlockElement rootNode;

    public LabelBlockPass(IdProvider idProvider) {
        this.idProvider = idProvider;
        rootNode = new LabelledCodeBlockElement(idProvider.next(), "ROOT", "HLASM_ROOT");
    }

    public LabelledCodeBlockElement run(List<String> lines) {
        currentBlock = rootNode;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String labelZone = line.substring(0, 8);
            if (isCSECT(line) || isDSECT(line) || labelZone.trim().isEmpty() || labelZone.trim().equals("SORTED"))
                addToCurrentBlock(line.substring(8));
            else if (labelZone.startsWith("*")) addCommentToCurrentBlock(line);
            else if (labelZone.trim().startsWith("&")) addCommentToCurrentBlock(line); // Ignore wholesale macro DSECT/CSECT substitutions for now
            else if (new ExecSqlPatternMatcher().matches(line)) {
                addToCurrentBlock(line);
//                addSQLToCurrentBlock(line);
            }
            else if (labelZone.startsWith(" ")) addToCurrentBlock(line); // Probably a malformatted call, so don't truncate it
            else {
                startNewLabel(labelZone.trim());
                addToCurrentBlock(line.substring(8));
            }
        }

        return rootNode;
    }

    private static boolean isCSECT(String line) {
        return line.substring(8).trim().startsWith("CSECT");
    }

    private static boolean isDSECT(String line) {
        return line.substring(8).trim().contains("DSECT");
    }

    private void addCommentToCurrentBlock(String line) {
        currentBlock.add(new CommentElement(idProvider.next(), line));
    }

    private void addToCurrentBlock(String line) {
        currentBlock.add(new RawCodeElement(idProvider.next(), line));
    }

    private void startNewLabel(String labelZone) {
        String uniqueLabelIfLocalised = labelZone.startsWith(".") ? labelZone + idProvider.next() : labelZone;
        // Calls to this label arent made unique yet, needs to happen in macro expansion
        LabelledCodeBlockElement newBlock = new LabelledCodeBlockElement(idProvider.next(), "LABEL", uniqueLabelIfLocalised);
        rootNode.add(newBlock);
        currentBlock = newBlock;
    }
}
