package com.mojo.loader;

import com.mojo.hlasm.HlasmFormatParserLexer;
import com.mojo.hlasm.HlasmFormatParserParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BuildHLASMParserGrammarMain {
    private static int ruleCounter = 1;

    public static void main(String[] args) {
        String formatFileName = "/Users/asgupta/code/hlasm-analyser/hlasm-parser/instruction_formats/HLASM Instruction Format.csv";
//        Stream<GeneratedParseRule> hlasmInstructionParserRules = formatFromCSV(formatFileName).map(BuildHLASMParserGrammarMain::asRule);
        Map<Pair<Integer, String>, String> operands = new HashMap<>();
        Stream<GeneratedParseRule> extraRules = Stream.of(new OverridingDefineConstantParseRule(), new OverridingDefineStructureParseRule());
        Stream<GeneratedParseRule> hlasmInstructionParserRules = new HLASMInstructionFormatBuilder().formatFromCSV(formatFileName).map(format -> asRule(format, operands));
        Stream<GeneratedParseRule> allHlasmInstructionParserRules = Stream.concat(hlasmInstructionParserRules, extraRules);

        List<Pair<String, GeneratedParseRule>> productionRules = allHlasmInstructionParserRules.map(BuildHLASMParserGrammarMain::asRuleString).distinct().toList();
        int instruction_group_size = 400;
        List<Pair<String, List<Pair<String, GeneratedParseRule>>>> instructionGroups = grouped(productionRules, instruction_group_size, 1).toList();
        List<String> instructionGroupStrings = instructionGroups.stream()
                .map(Pair::getLeft).toList();
        String instructionGroupsRule = String.join(" | ", instructionGroupStrings);
        Stream<String> instructionGroupContents = instructionGroups.stream().map(ig -> ig.getLeft() + ": " + String.join(" | ", ig.getRight().stream().map(Pair::getLeft).toList()) + ";");
        String assemblerInstructionRule = "assemblerInstruction: " + instructionGroupsRule + ";";

        try {
            String content = Files.readString(Path.of("/Users/asgupta/code/hlasm-analyser/hlasm-parser/grammar/HLASMPredefinitionsFragment.g4fragment"));
            Stream<String> topLevelRules = Stream.concat(Stream.of(content, assemblerInstructionRule), instructionGroupContents);
            topLevelRules.forEach(System.out::println);
            System.out.println("\n// Production rules\n");
            productionRules.stream().map(r -> r.getLeft() + ": " + r.getRight())
                    .forEach(rule -> System.out.println(rule + ";"));
            System.out.println("// Operand Definitions");
            operands.forEach((key, value) -> System.out.printf("%s: %s;%n", value, key.getRight()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Pair<String, List<Pair<String, GeneratedParseRule>>>> grouped(List<Pair<String, GeneratedParseRule>> productionRules, int instructionGroupSize, int counter) {
        if (productionRules.size() <= instructionGroupSize)
            return Stream.of(ImmutablePair.of("instruction_group_" + counter, productionRules));
        List<Pair<String, GeneratedParseRule>> chunk = productionRules.subList(0, instructionGroupSize);
        return Stream.concat(Stream.of(ImmutablePair.of("instruction_group_" + counter, chunk)),
                grouped(productionRules.subList(instructionGroupSize,
                        productionRules.size()), instructionGroupSize, counter + 1));
    }

    private static Pair<String, GeneratedParseRule> asRuleString(GeneratedParseRule rule) {
        return ImmutablePair.of(rule.opcode().toLowerCase() + "_rule_" + (ruleCounter++), rule);
    }

    private static GeneratedParseRule asRule(HLASMInstructionFormat format, Map<Pair<Integer, String>, String> operands) {
        ParseTree pattern = format.operandFormatTree();
        HLASMParseRuleBuilderVisitor<String> visitor = new HLASMParseRuleBuilderVisitor<>(operands);
        pattern.accept(visitor);
        return new GenerateHLASMParseRule(format, visitor.getFinalRules(), visitor.getLocalOperands());
    }

    private static ParseTree parsedFormat(String operandFormat) {
        CharStream charStream = CharStreams.fromString(operandFormat);
        HlasmFormatParserLexer lexer = new HlasmFormatParserLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HlasmFormatParserParser parser = new HlasmFormatParserParser(tokens);
        return parser.startRule();
    }

    private static Stream<HLASMInstructionFormat> formatFromCSV(String fileName) {
        try {
            FileReader in = new FileReader(fileName);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(in);
            return StreamSupport.stream(records.spliterator(), false)
                    .map(rec -> formatted(rec.get(0).replace("*", ""), rec.get(1).replaceAll("\\p{C}|\\s+", ""), rec.get(7)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HLASMInstructionFormat formatted(String opcode, String operandFormat, String description) {
//        System.out.println("Parsing: " + opcode + " " + operandFormat);
        return new HLASMInstructionFormat(opcode, operandFormat, parsedFormat(operandFormat), description);
    }
}
