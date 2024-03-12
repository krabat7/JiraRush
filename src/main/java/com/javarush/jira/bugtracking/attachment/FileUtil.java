package com.javarush.jira.bugtracking.attachment;

import com.javarush.jira.common.error.IllegalRequestDataException;
import com.javarush.jira.common.error.NotFoundException;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class FileUtil {
    private static final String ATTACHMENT_PATH = "./attachments/%s/";

    public static void upload(MultipartFile multipartFile, String directoryPath, String fileName) {
        if (multipartFile.isEmpty()) {
            throw new IllegalRequestDataException("Select a file to upload.");
        }

        try {
            Path directory = Path.of(directoryPath);
            Files.createDirectories(directory);

            Path filePath = directory.resolve(fileName);
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new IllegalRequestDataException("Failed to upload file " + multipartFile.getOriginalFilename());
        }
    }

    public static Resource download(String fileLink) {
        try {
            Path path = Path.of(fileLink);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IllegalRequestDataException("Failed to download file " + resource.getFilename());
            }
        } catch (IOException ex) {
            throw new NotFoundException("File " + fileLink + " not found");
        }
    }

    public static void delete(String fileLink) {
        try {
            Path path = Path.of(fileLink);
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            throw new IllegalRequestDataException("File " + fileLink + " deletion failed.");
        }
    }

    public static String getPath(String titleType) {
        return String.format(ATTACHMENT_PATH, titleType.toLowerCase());
    }
}
