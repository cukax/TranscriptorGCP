package com.ese.trancriptor.application.service;

import com.ese.trancriptor.domain.ports.out.CloudStoragePort;
import com.ese.trancriptor.domain.ports.out.FileStoragePort;
import com.ese.trancriptor.domain.ports.out.SpeechToTextPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranscriptionServiceTest {

    @Mock
    private FileStoragePort fileStoragePort;

    @Mock
    private SpeechToTextPort speechToTextPort;

    @Mock
    private CloudStoragePort cloudStoragePort;

    @InjectMocks
    private TranscriptionService transcriptionService;

    @Test
    void whenProcessAudios_thenUploadTranscribeAndDelete() {
        // Arrange
        String fileName = "audio.flac";
        byte[] audioData = new byte[]{1, 2, 3};
        String transcription = "Hola mundo";
        String bucketName = "audios-juntas";
        String gcsUri = "gs://audios-juntas/audio.flac";
        
        when(fileStoragePort.listFiles("input")).thenReturn(List.of(fileName));
        when(fileStoragePort.readFile("input/" + fileName)).thenReturn(audioData);
        when(cloudStoragePort.listFiles(bucketName, ".flac")).thenReturn(List.of(fileName));
        when(speechToTextPort.transcribeFromGcs(gcsUri)).thenReturn(transcription);

        // Act
        transcriptionService.processAudios();

        // Assert
        verify(fileStoragePort).listFiles("input");
        verify(fileStoragePort).readFile("input/" + fileName);
        verify(cloudStoragePort).uploadFile(bucketName, fileName, audioData);
        verify(cloudStoragePort).listFiles(bucketName, ".flac");
        verify(speechToTextPort).transcribeFromGcs(gcsUri);
        verify(fileStoragePort).writeFile(eq("output/audio.txt"), eq(transcription));
        verify(cloudStoragePort).deleteFile(bucketName, fileName);
    }

    @Test
    void whenNoFiles_thenDoNothing() {
        // Arrange
        when(fileStoragePort.listFiles("input")).thenReturn(Collections.emptyList());
        when(cloudStoragePort.listFiles("audios-juntas", ".flac")).thenReturn(Collections.emptyList());

        // Act
        transcriptionService.processAudios();

        // Assert
        verify(fileStoragePort).listFiles("input");
        verify(cloudStoragePort).listFiles("audios-juntas", ".flac");
        verifyNoMoreInteractions(speechToTextPort, fileStoragePort, cloudStoragePort);
    }
}
