package com.ese.trancriptor.domain.ports.out;

import java.util.List;

public interface CloudStoragePort {
    void uploadFile(String bucketName, String objectName, byte[] content);
    List<String> listFiles(String bucketName, String extension);
    void deleteFile(String bucketName, String objectName);
}
