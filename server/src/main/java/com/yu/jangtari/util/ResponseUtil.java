package com.yu.jangtari.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@UtilityClass
public class ResponseUtil {

    public void doResponse(HttpServletResponse response
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
