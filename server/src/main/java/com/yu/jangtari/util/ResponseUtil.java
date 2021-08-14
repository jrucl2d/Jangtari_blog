package com.yu.jangtari.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtil {

    public static void doResponse(HttpServletResponse response
        , Object responseBody
        , HttpStatus status) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(responseBody);
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(content);
    }
}
