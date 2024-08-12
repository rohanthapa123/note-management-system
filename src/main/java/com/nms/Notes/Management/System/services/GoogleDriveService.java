package com.nms.Notes.Management.System.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveService {

    private static final String APPLICATION_NAME = "Notes-Management-System";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = getPathToGoogleCredentials();

    private static String getPathToGoogleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "google-service-account.json");
        return filePath.toString();
    }

    public Drive createDriveService() throws IOException, GeneralSecurityException {


        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .build();
    }




    public String uploadFileToDrive(MultipartFile multipartfile, String mimeType) throws IOException, GeneralSecurityException {
        try {

            Path tempDir = Files.createTempDirectory("");
            java.io.File tempFile = tempDir.resolve(multipartfile.getOriginalFilename()).toFile();
            multipartfile.transferTo(tempFile);


            String folderId = "12FrrF1yO9q1LadxPHRLw9inZGgi292bR";

            Drive drive = createDriveService();

            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();


            fileMetadata.setName(tempFile.getName());
            fileMetadata.setParents(Collections.singletonList(folderId));

            FileContent mediaContent = new FileContent(mimeType, tempFile);

            File uploadedFile = createDriveService().files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            return uploadedFile.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Google Drive: " + e.getMessage(), e);
        }
    }
}
