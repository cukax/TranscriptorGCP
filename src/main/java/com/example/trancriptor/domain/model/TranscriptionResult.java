package com.example.trancriptor.domain.model;

public class TranscriptionResult {
    private final String fileName;
    private final String content;

    public TranscriptionResult(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }
}
