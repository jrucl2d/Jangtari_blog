package com.yu.jangtari.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class GDTestController {

//    @Autowired
//    private GoogleDriveUtil googleDriveUtil;
//
//    @GetMapping("/haha")
//    public void createFile() throws IOException, GeneralSecurityException {
//        Drive drive = googleDriveUtil.getDrive();
//        File file = new File();
//        file.setName("sample.jpg");
//        file.setParents(Collections.singletonList(googleDriveUtil.CATEGORY_FOLDER));
//        FileContent content = new FileContent("image/jpeg", new java.io.File("C:\\Users\\ysk78\\Pictures\\IU\\6578134C-ADE3-428A-9644-949D1AFFF0EC.jpeg"));
//        File uploadedFile = drive.files().create(file, content).setFields("id").execute();
//
//        String fileRef = String.format("{fileID : '%s'}", uploadedFile.getId());
//        System.out.println(fileRef);
//    }
}
