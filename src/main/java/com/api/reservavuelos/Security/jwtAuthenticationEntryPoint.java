package com.api.reservavuelos.Security;

import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Utils.DateFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class jwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final DateFormatter dateFormatter;

    public jwtAuthenticationEntryPoint(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimeStamp(dateFormatter.formatearFecha());
        responseDTO.setCode("P-403");
        responseDTO.setMessage("No estas autorizado para realizar esta accion");
        responseDTO.setUrl(request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDTO));
    }
}
