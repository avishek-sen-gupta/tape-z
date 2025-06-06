package com.mojo.loader.main;

import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.string.VerbatimBasicBlockTextMaker;
import com.mojo.visualisation.AIBasicBlockTextMaker;
import com.mojo.visualisation.BuildFlowchartPerSectionTask;
import com.mojo.woof.llm.OllamaAdvisor;
import com.mojo.woof.llm.OllamaCredentials;

public class HLASMFlowchartPerSectionMain {
    public static void main(String[] args) {
        String filePath = "/Users/asgupta/code/asmcode/broken.txt";
        String copybookPath = "/Users/asgupta/code/asmcode/Assembler Copybook";
        UUIDProvider idProvider = new UUIDProvider();
        new BuildFlowchartPerSectionTask().run(idProvider, filePath, copybookPath, "/Users/asgupta/code/tape-z/output", new VerbatimBasicBlockTextMaker(), "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol");

        System.out.println("COMPLETE!");
    }
}
