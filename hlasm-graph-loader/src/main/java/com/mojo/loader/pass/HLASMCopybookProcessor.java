package com.mojo.loader.pass;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Streams.zip;

public class HLASMCopybookProcessor {
    private static final Logger logger = LoggerFactory.getLogger(HLASMCopybookProcessor.class);
    public List<String> run(Path macroPath, List<String> macroDetails) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(macroPath.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        logger.debug("Getting parameters for macro copybook path: " + macroPath);
//        Pattern pattern = Pattern.compile("[A-Z0-9]+\\s(&[A-Z]+\\s)*");

        Pattern pattern = Pattern.compile("&\\w+");
        Matcher matcher = pattern.matcher(lines.get(1));
        List<String> matches = new ArrayList<>();
        while (matcher.find()) matches.add(matcher.group());
        if (matches.isEmpty()) {
            logger.debug("Nothing suitable to substitute...");
            return lines;
        }
        List<String> parameters = matches;
//        List<String> substitutors = parameters.stream().map(param -> "&" + param.trim()).toList();
        List<String> substitutors = parameters;
        List<String> parameterValues = new ArrayList<>();
        String[] splitUpValues = macroDetails.size() == 2 ? macroDetails.get(1).split(",") : new String[]{};
        for (int i = 0; i <= parameters.size() - 1; i++) {
            if (i >= splitUpValues.length) parameterValues.add("");
            else parameterValues.add(splitUpValues[i].replace(",", ""));
        }

        logger.debug("Parameters are for macro copybook path: " + macroPath + "=" + parameterValues);
        List<ImmutablePair<String, String>> paramsWithValues = zip(substitutors.stream(), parameterValues.stream(), ImmutablePair::of).toList();
        List<String> latestLines = lines;
        for (Pair<String, String> paramValue : paramsWithValues) {
            latestLines = lines.stream().map(l -> l.replace(paramValue.getLeft(), paramValue.getRight())).toList();
        }
        logger.debug("Completed substitution of '%s'...", macroPath);
        List<String> linesTruncatedBeyond72 = new DiscardAfter72Pass().run(latestLines);

        return linesTruncatedBeyond72;
    }
}
