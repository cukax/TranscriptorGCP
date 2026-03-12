package com.ese.trancriptor.domain.ports.out;

import java.util.List;

public interface FileStoragePort {
    List<String> listFiles(String directory);
    byte[] readFile(String fileName);
    void writeFile(String fileName, String content);
}
