package com.ese.trancriptor.infrastructure.adapters.out.file;

import com.ese.trancriptor.domain.ports.out.FileStoragePort;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileStorageAdapter implements FileStoragePort {

    @Override
    public List<String> listFiles(String directory) {
        File folder = new File(directory);
        if (!folder.exists() || !folder.isDirectory()) {
            return Collections.emptyList();
        }
        
        File[] files = folder.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        
        return Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] readFile(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + fileName, e);
        }
    }

    @Override
    public void writeFile(String fileName, String content) {
        try {
            Files.write(Paths.get(fileName), content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo: " + fileName, e);
        }
    }
}
