package com.api.reservavuelos.Filters;

import com.api.reservavuelos.Security.JwtTokenProvider;
import com.api.reservavuelos.Security.getTokenForRequest;
import com.api.reservavuelos.Utils.DateFormatter;
import com.api.reservavuelos.Utils.Url_WhiteList;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    private Date tiempoactual = new Date();


    @Autowired
    private DateFormatter dateFormatter;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    private Url_WhiteList urlWhiteList;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private getTokenForRequest GetTokenForRequest;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         String requestURI = request.getRequestURI();
        if(urlWhiteList.Url_whiteList().contains(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String  token = GetTokenForRequest.getTokenForRequest(request, response);
            jwtTokenProvider.IsValidToken(token);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException e){
           resolver.resolveException(request,response,null,e);
    }
    }
}
