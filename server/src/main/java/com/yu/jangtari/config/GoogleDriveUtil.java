package com.yu.jangtari.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.yu.jangtari.common.exception.FileTaskException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleDriveUtil {
// redirect URL을 적용해야 함
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private final String CATEGORY_FOLDER = "1NzhQFXNOqY3dNYIHq0Rpf6AKZH-xHWVR";
    private final String JANGTARI_FOLDER = "14CKUxuVzBqPz8RDwvLvUxaOJLI1Z62XK";
    private final String POST_FOLDER = "1G7FMqlteOguD-St7TOIO_P6czMDD46lS";
    private final String FILE_REF = "https://drive.google.com/uc?export=download&id=";

    private ClassPathResource gdSecretKeys = new ClassPathResource("/jangtari.json");
    private String token = "credentials";
    private String appname="jangtaritest";
    private GoogleAuthorizationCodeFlow flow;

    public GoogleAuthorizationCodeFlow getFlow(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(gdSecretKeys.getInputStream()));

        // Build flow and trigger user authorization request.
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(token)))
                .setAccessType("offline")
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        flow = getFlow(HTTP_TRANSPORT);

        return flow.loadCredential("user");
    }



    public List<String> fileToURL(List<MultipartFile> pictureFiles) throws GeneralSecurityException, IOException {
        Drive drive = getDrive();
        List<String> pictureURLs = pictureFiles.parallelStream().map(pictureFile -> {
            com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
            file.setName(getPictureName(pictureFile.getName()));
            file.setParents(Collections.singletonList(POST_FOLDER));
            java.io.File tempFile = null;
            String pictureURL = null;
            try {
                tempFile = convert(pictureFile);
                FileContent content = new FileContent("image/jpeg", tempFile);
                com.google.api.services.drive.model.File uploadedFile = drive.files().create(file, content).setFields("id").execute();
                tempFile.delete();
                pictureURL = FILE_REF + uploadedFile.getId();
            } catch (IOException e) {
                throw new FileTaskException(); // parellelStream 안에서 발생한 IOException
            }
            return pictureURL;
        }).collect(Collectors.toList());
        return pictureURLs;
    }

    private Drive getDrive() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(appname).build();
    }
    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    private String getPictureName(String pictureName) {
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd-HH:mm:ss");
        Date time = new Date();
        String dateString = format1.format(time);
        return dateString + "_" + pictureName + ".jpg";
    }
}
