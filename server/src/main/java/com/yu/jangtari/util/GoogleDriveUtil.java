package com.yu.jangtari.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.yu.jangtari.common.GDFolder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Profile("test")
@Service
public class GoogleDriveUtil {
    public GoogleAuthorizationCodeFlow getFlow(NetHttpTransport httpTransport) throws IOException {
        // not used
        return null;
    }

    public List<String> filesToURLs(List<MultipartFile> pictureFiles, GDFolder gdFolder) {
        if (pictureFiles == null || pictureFiles.isEmpty()) return Collections.emptyList();
        return pictureFiles.stream().map(MultipartFile::getName).collect(Collectors.toList());
    }

    public String fileToURL(MultipartFile pictureFile, GDFolder gdFolder) {
        if (pictureFile == null) return null;
        return pictureFile.getName();
    }
}
