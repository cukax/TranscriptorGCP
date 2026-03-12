## 📋 General Information

- **Project Name:** Trancriptor
- **Version:** 0.0.1-SNAPSHOT
- **Group:** com.ese
- **Description:** Project to transcribe audio files with Google Cloud Speech-to-Text
- **Java Version:** 21

## 🏗️ Project Architecture

This is a **Spring Boot** project focused on audio-to-text transcription, utilizing a **Hexagonal Architecture (Clean Architecture)**:

### Main Technologies

- **Spring Boot 3.4.3**
- **Google Cloud Speech-to-Text**
- **Google Cloud Storage**
- **Java 21**
- **JUnit 5 & Mockito**

## 📦 Main Dependencies

### Base Framework

- **Spring Boot Starter**: Spring Boot framework core
- **Google Cloud Libraries BOM (26.55.0)**: GCP version management

### Google Cloud Integration

- **Google Cloud Speech**: For audio transcription
- **Google Cloud Storage**: For cloud file storage

### Testing

- **Spring Boot Starter Test**: For unit and integration testing
- **Mockito**: For dependency mocking in tests

## 🔧 Project Configuration

### Google Cloud Configuration

The project is configured through the `src/main/resources/application.properties` file:

``` properties
spring.cloud.gcp.project-id=transcripcion-juntas
# spring.cloud.gcp.credentials.location=file:C:/path/to/your/credentials.json
```

## 📂 Project Structure

``` 
TranscriptorGCP/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ese/trancriptor/
│   │   │       ├── application/      # Application logic (services)
│   │   │       ├── domain/           # Entities and ports (interfaces)
│   │   │       └── infrastructure/   # Adapters and configuration
│   │   └── resources/
│   └── test/
├── input/      # Input audio files (.flac)
├── output/     # Transcription results (.txt)
├── pom.xml
└── README.md
```

## 🎯 Project Purpose

This project is designed as a tool to automate audio file transcription using Google Cloud services, including:

1. **Google Cloud SDK configuration with Spring Boot**
2. **Using Google Cloud Storage for file management**
3. **Asynchronous/synchronous transcription with Speech-to-Text**
4. **Hexagonal Architecture implementation** to decouple business logic from cloud providers

## 🚀 Running the Project

### Prerequisites

- Java 21
- Maven 3.6+
- Google Cloud Account and credentials (Service Account)

### Steps to run

1. Configure your GCP credentials in `application.properties` or via the `GOOGLE_APPLICATION_CREDENTIALS` environment variable.

2. Compile the project:

``` bash
   mvn clean compile
```

3. Run the application:

``` bash
   mvn spring-boot:run
```
