package com.mojo.visualisation;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.string.BasicBlockTextMaker;
import com.mojo.algorithms.string.TranspilerNodeFormatter;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.woof.llm.Advisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIBasicBlockTextMaker implements BasicBlockTextMaker {
    Logger LOGGER = LoggerFactory.getLogger(AIBasicBlockTextMaker.class.getName());
    private final Advisor advisor;
    private final TranspilerNodeFormatter textFormatter = new TranspilerNodeFormatter();

    public AIBasicBlockTextMaker(Advisor advisor) {
        this.advisor = advisor;
    }

    @Override
    public String format(BasicBlock<TranspilerInstruction> bb) {
//        if (bb.getInstructions().size() == 1 && bb.firstInstruction().ref() instanceof IfTranspilerNode)
//            return textFormatter.splitLines(textFormatter.squish(flowchartSourceText(bb.getInstructions().getFirst())), 15, "\n");
        String llmLineBreak = "\\n";
        String blockText = bb.getInstructions().stream()
                                   .map(instruction -> textFormatter.squish(flowchartSourceText(instruction)))
                                   .reduce("", (a, b) -> a + llmLineBreak + b) + llmLineBreak;
        String prompt = String.format("Summarize the purpose of the following HLASM code in a short sentence. Focus only on the main logic, subroutines called, decisions made and branches. Start directly with action verbs. Do not mention setup code, low-level instructions, or register operations. Avoid using HLASM-specific terminology. Clearly explain conditional logic, where EQ means equals and NE means not equals. For any SQL statements starting with 'EXEC SQL', reproduce them exactly as written in the code, maintaining their full syntax and parameters without summarizing or paraphrasing them.  Keep the sentence short and clear, suitable for display inside a flowchart node. Here is the code:  ```%s```", blockText);
        String response = advisor.advise(prompt).getFirst();
        LOGGER.info("Request is: " + prompt);
        LOGGER.info("Response is: " + response);
        return response;
    }

    private static String flowchartSourceText(TranspilerInstruction instruction) {
        if (instruction.ref() instanceof IfTranspilerNode ift)
            return "Is " + ift.getCondition().description() + "?";
        return instruction.originalText();
    }
}
