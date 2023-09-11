package com.application.hermesteamsphere.security;

import io.jsonwebtoken.*;

import static com.application.hermesteamsphere.util.Constantes.HEADER;
import static com.application.hermesteamsphere.util.Constantes.PREFIX;
import static com.application.hermesteamsphere.util.Constantes.SECRET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
    		throws ServletException, IOException {
       
    	try {
    		
            if (checkJWTToken(request)) {
            	
                Claims claims = validateToken(request);
                
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                }
                else {
                    SecurityContextHolder.clearContext();
                }
            }
            
            chain.doFilter(request, response);
        }
        catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			log.error("{}", e + " " + Arrays.toString(e.getStackTrace()));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = "Bearer INVALID";
        if(request.getHeader(HEADER)!=null)
        {
            jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    /**
     * Authentication method in Spring flow
     *
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
    	
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean checkJWTToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER);
        return !(authenticationHeader == null || !authenticationHeader.startsWith(PREFIX));
    }

}
