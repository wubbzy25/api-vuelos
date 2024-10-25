    package com.api.reservavuelos.Security;
    import com.api.reservavuelos.Utils.Url_WhiteList;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.NonNull;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;
    import org.springframework.web.servlet.HandlerExceptionResolver;

    import java.io.IOException;
    import java.util.List;

    @Component
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

         @Autowired
         private CustomUserDetailsService customUserDetailsService;
         @Autowired
         private JwtTokenProvider jwtTokenProvider;
         @Autowired
         private getTokenForRequest GetTokenForRequest;
         @Autowired
         @Qualifier("handlerExceptionResolver")
         private HandlerExceptionResolver resolver;
         @Autowired
         private Url_WhiteList urlWhiteList;

         @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
            String requestURI = request.getRequestURI();
            if(urlWhiteList.Url_whiteList().contains(requestURI)){
                filterChain.doFilter(request, response);
                return;
            }

            String  token = GetTokenForRequest.getToken(request, response);
            String email = jwtTokenProvider.getEmailFromToken(token);
           UserDetails userDetails =  customUserDetailsService.loadUserByUsername(email);
           List<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
             System.out.println(userDetails.getAuthorities());
           if(userRoles.contains("usuario") ||userRoles.contains("administrador") ){
               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                       null, userDetails.getAuthorities());
               authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
         filterChain.doFilter(request, response);

        }
    }
