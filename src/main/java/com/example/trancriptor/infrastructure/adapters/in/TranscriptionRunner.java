package com.example.trancriptor.infrastructure.adapters.in;

import com.example.trancriptor.domain.ports.in.TranscriptionUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TranscriptionRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TranscriptionRunner.class);

    private final TranscriptionUseCase transcriptionUseCase;

    public TranscriptionRunner(TranscriptionUseCase transcriptionUseCase) {
        this.transcriptionUseCase = transcriptionUseCase;
    }

    @Override
    public void run(String... args) {
        log.info("App iniciada. Procesando archivos...");
        transcriptionUseCase.processAudios();
        log.info("Procesamiento terminado.");
    }
}
