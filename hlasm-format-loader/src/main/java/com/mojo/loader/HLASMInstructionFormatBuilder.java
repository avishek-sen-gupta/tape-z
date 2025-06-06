package com.mojo.loader;

import com.mojo.hlasm.HlasmFormatParserLexer;
import com.mojo.hlasm.HlasmFormatParserParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HLASMInstructionFormatBuilder {
    public Stream<HLASMInstructionFormat> formatFromCSV(String fileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(reader);
            return StreamSupport.stream(records.spliterator(), false)
                    .map(rec -> mnemonic(rec.get(0).replace("*", ""), rec.get(1).replaceAll("\\p{C}|\\s+", ""), rec.get(7)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ParseTree parsedFormat(String operandFormat) {
        CharStream charStream = CharStreams.fromString(operandFormat);
        HlasmFormatParserLexer lexer = new HlasmFormatParserLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HlasmFormatParserParser parser = new HlasmFormatParserParser(tokens);
        return parser.startRule();
    }

    private HLASMInstructionFormat mnemonic(String opcode, String operandFormat, String description) {
//        System.out.println("Parsing: " + opcode + " " + operandFormat);
        return new HLASMInstructionFormat(opcode, operandFormat, parsedFormat(operandFormat), description);
    }
}
