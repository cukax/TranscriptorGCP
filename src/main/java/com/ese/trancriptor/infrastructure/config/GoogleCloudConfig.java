package com.ese.trancriptor.infrastructure.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudConfig {

    @Value("${spring.cloud.gcp.credentials.location:}")
    private String credentialsLocation;

    @Bean
    public SpeechClient speechClient() throws IOException {
        if (credentialsLocation != null && !credentialsLocation.isEmpty()) {
            GoogleCredentials credentials = getCredentials();
            SpeechSettings settings = SpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
            return SpeechClient.create(settings);
        }
        return SpeechClient.create();
    }

    @Bean
    public Storage storageClient() throws IOException {
        if (credentialsLocation != null && !credentialsLocation.isEmpty()) {
            return StorageOptions.newBuilder()
                .setCredentials(getCredentials())
                .build()
                .getService();
        }
        return StorageOptions.getDefaultInstance().getService();
    }

    private GoogleCredentials getCredentials() throws IOException {
        String path = credentialsLocation.startsWith("file:")
                ? credentialsLocation.substring(5)
                : credentialsLocation;
        return GoogleCredentials.fromStream(new FileInputStream(path));
    }
}
