package com.ese.trancriptor.infrastructure.adapters.out.googlecloud;

import com.ese.trancriptor.domain.ports.out.SpeechToTextPort;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class GoogleCloudSpeechToTextAdapter implements SpeechToTextPort {

    private static final Logger log = LoggerFactory.getLogger(GoogleCloudSpeechToTextAdapter.class);

    private final SpeechClient speechClient;

    public GoogleCloudSpeechToTextAdapter(SpeechClient speechClient) {
        this.speechClient = speechClient;
    }

    @Override
    public String transcribe(byte[] audioData) {
        ByteString audioBytes = ByteString.copyFrom(audioData);

        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(audioBytes)
                .build();

        return executeTranscription(audio);
    }

    @Override
    public String transcribeFromGcs(String gcsUri) {
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setUri(gcsUri)
                .build();

        return executeTranscription(audio);
    }

    private String executeTranscription(RecognitionAudio audio) {
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                .setAudioChannelCount(RecognitionConfig.AudioEncoding.FLAC.getNumber())
                .setSampleRateHertz(48000)
                .setLanguageCode("es-MX")
                .setDiarizationConfig(SpeakerDiarizationConfig.newBuilder()
                        .setEnableSpeakerDiarization(true)
                        .build())
                .build();

        log.info("Enviando audio a Google Cloud Speech-to-Text...");
        OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                speechClient.longRunningRecognizeAsync(config, audio);

        try {
            return response.get().getResultsList().stream()
                    .map(result -> result.getAlternativesList().getFirst().getTranscript())
                    .collect(Collectors.joining("\n"));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
