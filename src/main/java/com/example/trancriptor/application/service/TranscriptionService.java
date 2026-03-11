package com.example.trancriptor.application.service;

import com.example.trancriptor.domain.ports.in.TranscriptionUseCase;
import com.example.trancriptor.domain.ports.out.CloudStoragePort;
import com.example.trancriptor.domain.ports.out.FileStoragePort;
import com.example.trancriptor.domain.ports.out.SpeechToTextPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranscriptionService implements TranscriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(TranscriptionService.class);

    private final FileStoragePort fileStoragePort;
    private final SpeechToTextPort speechToTextPort;
    private final CloudStoragePort cloudStoragePort;

    private static final String INPUT_DIR = "input";
    private static final String OUTPUT_DIR = "output";
    private static final String BUCKET_NAME = "audios-juntas";
    private static final String AUDIO_EXTENSION = ".flac";

    public TranscriptionService(FileStoragePort fileStoragePort, 
                               SpeechToTextPort speechToTextPort,
                               CloudStoragePort cloudStoragePort) {
        this.fileStoragePort = fileStoragePort;
        this.speechToTextPort = speechToTextPort;
        this.cloudStoragePort = cloudStoragePort;
    }

    @Override
    public void processAudios() {
        log.info("Iniciando flujo de procesamiento de audios...");

        // 1. Subir archivos locales .flac a GCS
        List<String> localFiles = fileStoragePort.listFiles(INPUT_DIR).stream()
                .filter(f -> f.endsWith(AUDIO_EXTENSION))
                .toList();

        for (String fileName : localFiles) {
            try {
                byte[] content = fileStoragePort.readFile(INPUT_DIR + "/" + fileName);
                cloudStoragePort.uploadFile(BUCKET_NAME, fileName, content);
            } catch (Exception e) {
                log.error("Error subiendo {} a GCS: {}", fileName, e.getMessage());
            }
        }

        // 2. Listar archivos .flac en el bucket
        List<String> bucketFiles = cloudStoragePort.listFiles(BUCKET_NAME, AUDIO_EXTENSION);

        // 3. Procesar transcripción
        for (String fileName : bucketFiles) {
            try {
                log.info("Procesando archivo desde GCS: {}", fileName);
                String gcsUri = String.format("gs://%s/%s", BUCKET_NAME, fileName);
                String transcription = speechToTextPort.transcribeFromGcs(gcsUri);

                // Guardar resultado localmente
                int lastDotIndex = fileName.lastIndexOf('.');
                String baseName = (lastDotIndex == -1) ? fileName : fileName.substring(0, lastDotIndex);
                String outputFileName = baseName + ".txt";
                fileStoragePort.writeFile(OUTPUT_DIR + "/" + outputFileName, transcription);
                log.info("Transcripción completada para: {}", fileName);

                // 4. Borrar archivo exitoso de GCS
                cloudStoragePort.deleteFile(BUCKET_NAME, fileName);
            } catch (Exception e) {
                log.error("Error procesando el archivo de GCS {}: {}", fileName, e.getMessage());
            }
        }
    }
}
