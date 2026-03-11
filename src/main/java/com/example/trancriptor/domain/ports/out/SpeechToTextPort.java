package com.example.trancriptor.domain.ports.out;

public interface SpeechToTextPort {
    String transcribeFromGcs(String gcsUri);
}
