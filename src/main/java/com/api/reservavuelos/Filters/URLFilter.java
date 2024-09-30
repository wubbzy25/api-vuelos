package com.api.reservavuelos.Filters;


import com.api.reservavuelos.DTO.ResponseExceptionDTO;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class URLFilter extends OncePerRequestFilter {

    private Date tiempoactual = new Date();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DateFormatter dateFormatter;


    private static final List<String> URLS_LIST = Arrays.asList(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/reservas/vuelos",
            "/api/v1/auth/forgot-password"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        try {
            if (!URLS_LIST.contains(requestURI)) {
                throw new UrlNotFoundException();
            }
            filterChain.doFilter(request,response);
        } catch (UrlNotFoundException e) {

           String formattedDate = dateFormatter.formatearFecha(tiempoactual);

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
            responseExceptionDTO.setCode("P-400");
            responseExceptionDTO.setMessage("El Url no existe :/");
            responseExceptionDTO.setUrl(requestURI);
            responseExceptionDTO.setTimeStamp(formattedDate);

            String jsonResponse = objectMapper.writeValueAsString(responseExceptionDTO);
            response.getWriter().write(jsonResponse);
        }
    }

}
