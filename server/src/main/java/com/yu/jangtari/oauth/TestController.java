package com.yu.jangtari.oauth;

import com.yu.jangtari.common.GDFolder;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final GoogleDriveUtil googleDriveUtil;

    @PostMapping(value = "/test/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String testPicture(@RequestParam(required = false, name = "picture") MultipartFile picture) {
        return googleDriveUtil.fileToURL(picture, GDFolder.POST);
    }
}
