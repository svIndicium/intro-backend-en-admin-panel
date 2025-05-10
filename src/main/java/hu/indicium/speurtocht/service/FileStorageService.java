package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.FileSubmission;
import hu.indicium.speurtocht.repository.FileSubmissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.storage.location}")
    private String storagePath;

    private FileSubmissionRepository repository;

    public FileStorageService(FileSubmissionRepository repository) {
        this.repository = repository;
    }

    public FileSubmission saveFile(MultipartFile file) throws IOException {

        FileSubmission save = this.repository.save(new FileSubmission(file));
        try {
            Path destination = Paths.get(storagePath, save.getId().toString());
            Files.copy(file.getInputStream(), destination);
        } catch (IOException e) {
            e.printStackTrace();
            this.repository.delete(save);
            throw e;
        }

        return save;
    }

    public FileSubmission saveFile(MultipartFile file, String fileType) throws IOException {
        log.info(storagePath);
        FileSubmission save = this.repository.save(new FileSubmission(fileType));
        try {
            Path destination = Paths.get(storagePath, save.getId().toString());
            Files.copy(file.getInputStream(), destination);
        } catch (IOException e) {
            e.printStackTrace();
            this.repository.delete(save);
            throw e;
        }

        return save;
    }

    public FileSubmission getSubmissionFile(UUID id) {
        return this.repository.getReferenceById(id);
    }

    public byte[] getFile(String filename) throws IOException {
        Path filePath = Paths.get(storagePath, filename);
        return Files.readAllBytes(filePath);
    }

    public InputStream getFileOptimized(String filename) throws IOException {
        Path filePath = Paths.get(storagePath, filename);
        return Files.newInputStream(filePath);
    }

    public long getFileSize(String filename) {
        return Paths.get(storagePath, filename).toFile().length();
    }

    public FileSubmission saveFile(InputStream byteArrayInputStream, String fileType) throws IOException {

        FileSubmission save = this.repository.save(new FileSubmission(fileType));
        try {
            Path destination = Paths.get(storagePath, save.getId().toString());
            Files.copy(byteArrayInputStream, destination);
        } catch (IOException e) {
            e.printStackTrace();
            this.repository.delete(save);
            throw e;
        }

        return save;
    }

    public FileSubmission preReserveFileLocation(String fileType) {
        return this.repository.save(new FileSubmission(fileType));
    }
}
