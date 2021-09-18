package com.yu.jangtari.util;

import com.google.api.client.auth.oauth2.Credential;
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
import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Profile("dev")
public class GoogleDriveUtil {
    // redirect URL을 적용해야 함
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    // category, jangtari, post
    @Value("${jangtari.drive.list}")
    private List<String> folders;

    private final ClassPathResource gdSecretKeys = new ClassPathResource("/jangtari.json");
    private static final String token = "credentials";
    private static final String appname = "jangtaritest";

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

    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        GoogleAuthorizationCodeFlow flow = getFlow(httpTransport);
        return flow.loadCredential("user");
    }

    /**
     * Google Drive API를 사용해 파일을 구글 드라이브에 저장하고 저장한 URL을 리턴해주는 메소드
     * 사진 파일이 없으면(null) null을 리턴
     * @param pictureFiles 사진 파일
     * @param gdFolder     카테고리/포스트/사람 사진
     * @return URL의 리스트 / URL
     */
    public List<String> filesToURLs(List<MultipartFile> pictureFiles, GDFolder gdFolder) {
        if (pictureFiles == null || pictureFiles.isEmpty()) return Collections.emptyList();
        List<String> pictureURLs;
        try {
            Drive drive = getDrive();
            pictureURLs = pictureFiles.parallelStream().map(pictureFile -> getURL(pictureFile, gdFolder, drive)).collect(Collectors.toList());
        } catch (GeneralSecurityException e) {
            throw new BusinessException(ErrorCode.GOOGLE_DRIVE_ERROR);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_TASK_ERROR);
        }
        return pictureURLs;
    }

    public String fileToURL(MultipartFile pictureFile, GDFolder gdFolder) {
        if (pictureFile == null) return null;
        try {
            Drive drive = getDrive();
            return getURL(pictureFile, gdFolder, drive);
        } catch (GeneralSecurityException e) {
            throw new BusinessException(ErrorCode.GOOGLE_DRIVE_ERROR);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_TASK_ERROR);
        }
    }

    private String getURL(MultipartFile pictureFile, GDFolder gdFolder, Drive drive) {
        final com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
        file.setName(getPictureName(pictureFile.getName()));
        file.setParents(Collections.singletonList(folders.get(gdFolder.getNumber())));
        final File tempFile;
        try {
            tempFile = new File(Objects.requireNonNull(pictureFile.getOriginalFilename()));
        } catch (NullPointerException e) {
            throw new BusinessException(ErrorCode.FILE_TASK_ERROR);
        }

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(pictureFile.getBytes());
            final FileContent content = new FileContent("image/jpeg", tempFile);
            final com.google.api.services.drive.model.File uploadedFile = drive.files().create(file, content).setFields("id").execute();
            String fileRef = "https://drive.google.com/uc?export=download&id=";
            return fileRef + uploadedFile.getId();
        } catch (IOException | NullPointerException e) {
            throw new BusinessException(ErrorCode.FILE_TASK_ERROR);
        }
    }


    private Drive getDrive() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport)).setApplicationName(appname).build();
    }

    private String getPictureName(final String pictureName) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String dateString = format.format(new Date());
        return dateString + "_" + pictureName + ".jpg";
    }
}
