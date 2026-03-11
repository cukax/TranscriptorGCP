package com.example.trancriptor.infrastructure.adapters.out.googlecloud;

import com.example.trancriptor.domain.ports.out.CloudStoragePort;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoogleCloudStorageAdapter implements CloudStoragePort {

    private static final Logger log = LoggerFactory.getLogger(GoogleCloudStorageAdapter.class);
    private final Storage storage;

    public GoogleCloudStorageAdapter(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void uploadFile(String bucketName, String objectName, byte[] content) {
        log.info("Subiendo archivo {} al bucket {}", objectName, bucketName);
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, content);
    }

    @Override
    public List<String> listFiles(String bucketName, String extension) {
        log.info("Listando archivos con extensión {} en el bucket {}", extension, bucketName);
        List<String> fileList = new java.util.ArrayList<>();
        Iterable<Blob> blobs = storage.list(bucketName).iterateAll();
        for (Blob blob : blobs) {
            if (blob.getName().endsWith(extension)) {
                fileList.add(blob.getName());
            }
        }
        return fileList;
    }

    @Override
    public void deleteFile(String bucketName, String objectName) {
        log.info("Eliminando archivo {} del bucket {}", objectName, bucketName);
        BlobId blobId = BlobId.of(bucketName, objectName);
        storage.delete(blobId);
    }
}
