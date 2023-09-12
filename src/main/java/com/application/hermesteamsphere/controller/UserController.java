package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.UserRestDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.application.hermesteamsphere.util.Constantes.*;

@RestController
public class UserController {
	
	private static final String USER_SEPARATOR = "_";
    private static final long EXPIRATION_TIME = 10 * 60 * 1000L; // 10 minutos

    @PostMapping("user")
    public ResponseEntity<String> loginNew(@RequestBody UserRestDTO us) {
        // Ficticio, deberá recuperarse de la BD
        // Obtener el user con el code indicado y
        // comparar si la password es la misma
        User user = new User();
        user.setId(1L);
        user.setCode(us.getCode());
        user.setName("name");

    	String token = getTokenForUser(user, true);
    	return ResponseEntity.accepted().body(token);
    }
    
	public static String getTokenForUser(User user, boolean expiration) {
		
		String[] rolesForUser = new String[1];
        rolesForUser[0] = "ACCESO_API";

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(rolesForUser);

        String subject = user.getName() + USER_SEPARATOR + user.getId();

        String token;
        if (expiration)
        {
            token = Jwts.builder().setId(TOKEN_ID).setSubject(subject)
                    .claim("authorities",
                            grantedAuthorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
        }
        else
        {
            token = Jwts.builder().setId(TOKEN_ID).setSubject(subject)
                    .claim("authorities",
                            grantedAuthorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
        }

        return PREFIX + token;
    }

}