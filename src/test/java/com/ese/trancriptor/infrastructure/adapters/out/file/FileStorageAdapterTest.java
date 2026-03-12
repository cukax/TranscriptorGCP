package com.ese.trancriptor.infrastructure.adapters.out.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageAdapterTest {

    private FileStorageAdapter adapter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        adapter = new FileStorageAdapter();
    }

    @Test
    void testListFiles() throws IOException {
        Files.createFile(tempDir.resolve("test1.wav"));
        Files.createFile(tempDir.resolve("test2.wav"));
        Files.createDirectory(tempDir.resolve("subdir"));

        List<String> files = adapter.listFiles(tempDir.toString());

        assertEquals(2, files.size());
        assertTrue(files.contains("test1.wav"));
        assertTrue(files.contains("test2.wav"));
    }

    @Test
    void testReadAndWrite() throws IOException {
        String fileName = tempDir.resolve("output.txt").toString();
        String content = "Transcripción de prueba";

        adapter.writeFile(fileName, content);
        
        byte[] readData = adapter.readFile(fileName);
        assertEquals(content, new String(readData));
    }
}
