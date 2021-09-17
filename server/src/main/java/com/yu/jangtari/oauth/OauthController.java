package com.yu.jangtari.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.yu.jangtari.util.GoogleDriveUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final GoogleDriveUtil googleDriveUtil;

    @Value("${jangtari.oauth.callback}")
    private String redirectURI;

    @GetMapping("/oauthMaker")
    public void getOauth(HttpServletResponse response) throws Exception {
        log.info("oauth 가져오기");
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleAuthorizationCodeFlow flow = googleDriveUtil.getFlow(HTTP_TRANSPORT);
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(redirectURI).setAccessType("offline").build();
        log.info("oauth 가져오기 요청 보냄");
        response.sendRedirect(redirectURL);
    }

    @GetMapping("/Callback")
    public void saveAuthorizationCode(HttpServletRequest request) throws IOException, GeneralSecurityException {
        log.info("oauth callback 받고 code 저장");
        String code = request.getParameter("code");
        if(code != null){
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            GoogleAuthorizationCodeFlow flow = googleDriveUtil.getFlow(HTTP_TRANSPORT);
            GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            flow.createAndStoreCredential(response, "user");
            log.info("credentials 파일 저장");
        }
    }
}
