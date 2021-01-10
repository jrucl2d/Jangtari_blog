package com.yu.jangtari.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
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

@Service
public class GoogleDriveUtil {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    public final String CATEGORY_FOLDER = "1NzhQFXNOqY3dNYIHq0Rpf6AKZH-xHWVR";
    public final String JANGTARI_FOLDER = "14CKUxuVzBqPz8RDwvLvUxaOJLI1Z62XK";
    public final String POST_FOLDER = "1G7FMqlteOguD-St7TOIO_P6czMDD46lS";

    public final String FILE_REF = "https://drive.google.com/uc?export=download&id=";

    private ClassPathResource gdSecretKeys = new ClassPathResource("/jangtari.json");
    private ClassPathResource credentialsFolder = new ClassPathResource("/credentials");
    private String appname="jangtaritest";
    private GoogleAuthorizationCodeFlow flow;

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(gdSecretKeys.getInputStream()));

        InputStream inputStream = credentialsFolder.getInputStream();
        File file = new File("/credentials");
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buf)) > 0){
            outputStream.write(buf, 0, len);
        }
        outputStream.close();
        inputStream.close();

        // Build flow and trigger user authorization request.
        flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(file))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        file.delete();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Drive getDrive() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(appname).build();
    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public String getPictureName(String pictureName) {
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd-HH:mm:ss");
        Date time = new Date();
        String dateString = format1.format(time);
        return dateString + "_" + pictureName + ".jpg";
    }
}
