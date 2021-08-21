package com.yu.jangtari.testHelper;

import org.springframework.mock.web.MockMultipartFile;

public class MultipartUtil
{
    public static MockMultipartFile createOne(String name) {
        return new MockMultipartFile(name, new byte[] {0,});
    }
}
