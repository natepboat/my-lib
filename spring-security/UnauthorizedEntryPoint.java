package com.app.vote.security;

import com.app.vote.model.ErrorCode;
import com.app.vote.model.StatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Setter(onMethod = @__(@Autowired))
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = 7666235439777629914L;
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        StatusResponse body = new StatusResponse();
        body.setStatus("error");
        body.setErrorCode(ErrorCode.UNAUTHORIZED.getCode());
        body.setMessage(ErrorCode.UNAUTHORIZED.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(objectMapper.writeValueAsString(body));
    }
}
