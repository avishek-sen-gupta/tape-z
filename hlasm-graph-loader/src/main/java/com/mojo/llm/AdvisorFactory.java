package com.mojo.llm;

import com.mojo.algorithms.string.BasicBlockTextMaker;
import com.mojo.algorithms.string.VerbatimBasicBlockTextMaker;
import com.mojo.visualisation.AIBasicBlockTextMaker;
import com.mojo.woof.llm.*;

import java.util.Optional;

public class AdvisorFactory {

    public static final String AZURE_OPENAI = "AZURE_OPENAI";
    public static final String AWS_BEDROCK = "AWS_BEDROCK";
    public static final String OLLAMA = "OLLAMA";

    public static BasicBlockTextMaker advisor(String model) {
        return Optional.ofNullable(model).map(AdvisorFactory::advisorForModel).orElse(new VerbatimBasicBlockTextMaker());
    }

    private static BasicBlockTextMaker advisorForModel(String model) {
        return switch (model.trim().toUpperCase()) {
            case AZURE_OPENAI -> new AIBasicBlockTextMaker(new AzureOpenAIAdvisor(OpenAICredentials.fromEnv()));
            case AWS_BEDROCK -> new AIBasicBlockTextMaker(new AWSAdvisor(AWSCredentials.fromEnv()));
            case OLLAMA -> new AIBasicBlockTextMaker(new OllamaAdvisor(OllamaCredentials.fromEnv()));
            default -> new VerbatimBasicBlockTextMaker();
        };
    }
}
