package com.yu.jangtari.testHelper;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PictureFileUtil {
    public static MultipartFile createOne(String name) {
        return new MockMultipartFile(name, name.getBytes(StandardCharsets.UTF_8));
    }

    public static List<MultipartFile> createList(String... names) {
        return Arrays.stream(names).map(PictureFileUtil::createOne).collect(Collectors.toList());
    }
}
