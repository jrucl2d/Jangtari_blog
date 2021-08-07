package com.yu.jangtari.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.yu.jangtari.util.GoogleDriveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
public class OauthController {

    @Autowired
    private GoogleDriveUtil googleDriveUtil;

    private final String redirectURI = "http://www.gamsk.kro.kr/Callback";

    @GetMapping("/oauthMaker")
    public void getOauth(HttpServletResponse response) throws Exception{
        System.out.println("오어스 시작");
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleAuthorizationCodeFlow flow = googleDriveUtil.getFlow(HTTP_TRANSPORT);
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(redirectURI).setAccessType("offline").build();
        System.out.println("요청 보냄");
        response.sendRedirect(redirectURL);
    }

    @GetMapping("/Callback")
    public void saveAuthorizationCode(HttpServletRequest request) throws IOException, GeneralSecurityException {
        System.out.println("콜백 받고 코드 저장");
        String code = request.getParameter("code");
        if(code != null){
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            GoogleAuthorizationCodeFlow flow = googleDriveUtil.getFlow(HTTP_TRANSPORT);
            GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            flow.createAndStoreCredential(response, "user");
            System.out.println("크레덴셜 저장");
        }
    }
}
