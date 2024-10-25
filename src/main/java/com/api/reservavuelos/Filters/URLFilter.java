package com.api.reservavuelos.Filters;


import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Exceptions.MethodNotAllowedException;
import com.api.reservavuelos.Exceptions.UrlNotFoundException;
import com.api.reservavuelos.Utils.DateFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class URLFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DateFormatter dateFormatter;

    private static final Map<String, String> URLS_MAP = new HashMap<>();

    static {
        URLS_MAP.put("/api/v1/auth/register", "POST");
        URLS_MAP.put("/api/v1/auth/login", "POST");
        URLS_MAP.put("/api/v1/auth/forgot-password", "GET");
        URLS_MAP.put("/api/v1/auth/verify-code", "POST");
        URLS_MAP.put("/api/v1/auth/change-password", "POST");
        URLS_MAP.put("/api/v1/auth/2FA/setup", "GET");
        URLS_MAP.put("/api/v1/auth/2FA/verify", "POST");
        URLS_MAP.put("/api/v1/profile/upload-image", "POST");
        URLS_MAP.put("/api/v1/profile/\\d+", "GET");
        URLS_MAP.put("/api/v1/profile/edit-profile/\\d+", "PUT");
        URLS_MAP.put("/api/v1/metodos/\\d+", "GET");
        URLS_MAP.put("/api/v1/metodos/\\d+/\\d+", "GET");
        URLS_MAP.put("/api/v1/metodos/crear/\\d+", "POST");
        URLS_MAP.put("/api/v1/metodos/editar/\\d+/\\d+", "PUT");
        URLS_MAP.put("/api/v1/metodos/eliminar/\\d+/\\d+", "DELETE");
        URLS_MAP.put("/api/v1/vuelos", "GET");
        URLS_MAP.put("/api/v1/vuelos/vuelo/\\d+", "GET");
        URLS_MAP.put("/api/v1/vuelos/vuelo/crear", "POST");
        URLS_MAP.put("/api/v1/vuelos/vuelo/actualizar-estado/\\d+", "PUT");
        URLS_MAP.put("/api/v1/vuelos/vuelo/editar/\\d+", "PUT");
        URLS_MAP.put("/api/v1/vuelos/vuelo/eliminar/\\d+", "DELETE");
        URLS_MAP.put("/api/v1/reservas/reservar", "POST");
    }

    private void ExceptionHandler(HttpServletResponse response, String code, String message, String requestURI) throws IOException {
        String formattedDate = dateFormatter.formatearFecha();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(code);
        responseDTO.setMessage(message);
        responseDTO.setUrl(requestURI);
        responseDTO.setTimeStamp(formattedDate);

        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        boolean urlMatched = false;

        try {
            for (Map.Entry<String, String> entry : URLS_MAP.entrySet()) {
                if (requestURI.matches(entry.getKey()) && requestMethod.equals(entry.getValue())) {
                    urlMatched = true;
                    break;
                }
            }

            if (!urlMatched) {
                throw new UrlNotFoundException();
            }
            filterChain.doFilter(request, response);
        } catch (UrlNotFoundException | MethodNotAllowedException e) {
            ExceptionHandler(response, "404", "URL no Existe :/", requestURI);
        }
    }
}
