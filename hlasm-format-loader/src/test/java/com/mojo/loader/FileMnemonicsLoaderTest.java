package com.mojo.loader;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileMnemonicsLoaderTest {

    @Test
    public void testMnemonicsLoading() {
        // Create an instance of FileMnemonicsLoader
        FileMnemonicsLoader loader = new FileMnemonicsLoader();

        // Call the mnemonics method which should load the CSV file
        List<String> mnemonics = loader.mnemonics();

        // Verify that the list is not null and not empty
        assertNotNull(mnemonics, "Mnemonics list should not be null");
        assertFalse(mnemonics.isEmpty(), "Mnemonics list should not be empty");

        // Print the first few mnemonics for debugging
        System.out.println("[DEBUG_LOG] First 5 mnemonics (or fewer if less available):");
        mnemonics.stream().limit(5).forEach(m -> System.out.println("[DEBUG_LOG] " + m));
    }
}
