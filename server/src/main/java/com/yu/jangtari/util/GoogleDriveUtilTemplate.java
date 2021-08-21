package com.yu.jangtari.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.yu.jangtari.common.GDFolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoogleDriveUtilTemplate {
    GoogleAuthorizationCodeFlow getFlow(NetHttpTransport httpTransport) throws IOException;
    List<String> filesToURLs(List<MultipartFile> pictureFiles, GDFolder gdFolder);
    String fileToURL(MultipartFile pictureFile, GDFolder gdFolder);
}
