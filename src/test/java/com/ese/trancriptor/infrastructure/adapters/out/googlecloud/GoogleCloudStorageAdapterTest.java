package com.ese.trancriptor.infrastructure.adapters.out.googlecloud;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleCloudStorageAdapterTest {

    private Storage storage;
    private Blob blob;
    private GoogleCloudStorageAdapter adapter;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class, withSettings().strictness(Strictness.LENIENT));
        blob = mock(Blob.class, withSettings().strictness(Strictness.LENIENT));
        adapter = new GoogleCloudStorageAdapter(storage);
    }

    @Test
    void uploadFile_shouldCallStorageCreate() {
        String bucketName = "test-bucket";
        String objectName = "test.flac";
        byte[] content = "test content".getBytes();

        adapter.uploadFile(bucketName, objectName, content);

        verify(storage).create(any(com.google.cloud.storage.BlobInfo.class), eq(content));
    }

    @Test
    void listFiles_shouldReturnFilteredFiles() {
        String bucketName = "test-bucket";
        String extension = ".flac";
        
        com.google.api.gax.paging.Page<Blob> page = mock(com.google.api.gax.paging.Page.class);
        when(storage.list(bucketName)).thenReturn(page);
        when(page.iterateAll()).thenReturn(List.of(blob));
        when(blob.getName()).thenReturn("file1.flac");

        List<String> files = adapter.listFiles(bucketName, extension);

        assertEquals(1, files.size());
        assertEquals("file1.flac", files.get(0));
    }

    @Test
    void deleteFile_shouldCallStorageDelete() {
        String bucketName = "test-bucket";
        String objectName = "test.flac";

        adapter.deleteFile(bucketName, objectName);

        verify(storage).delete(any(com.google.cloud.storage.BlobId.class));
    }
}
