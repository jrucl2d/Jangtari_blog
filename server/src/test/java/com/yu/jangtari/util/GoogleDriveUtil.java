package com.yu.jangtari.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.yu.jangtari.common.GDFolder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Profile("test")
@Service
public class GoogleDriveUtil implements GoogleDriveUtilTemplate {
    @Override
    public GoogleAuthorizationCodeFlow getFlow(NetHttpTransport httpTransport) throws IOException
    {
        // not used
        return null;
    }

    @Override
    public List<String> filesToURLs(List<MultipartFile> pictureFiles, GDFolder gdFolder)
    {
        return pictureFiles.stream().map(MultipartFile::getName).collect(Collectors.toList());
    }

    @Override
    public String fileToURL(MultipartFile pictureFile, GDFolder gdFolder)
    {
        return pictureFile.getName();
    }
}
