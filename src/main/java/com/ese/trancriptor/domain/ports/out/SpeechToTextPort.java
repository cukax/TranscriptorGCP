package com.ese.trancriptor.domain.ports.out;

public interface SpeechToTextPort {
    String transcribeFromGcs(String gcsUri);
}
