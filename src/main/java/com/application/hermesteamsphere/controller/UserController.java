package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.UserDTO;
import com.application.hermesteamsphere.dto.UserRestDTO;
import com.application.hermesteamsphere.repositories.UserRepository;
import com.application.hermesteamsphere.services.CompanyService;
import com.application.hermesteamsphere.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.application.hermesteamsphere.util.Constantes.*;

@RestController("user")
public class UserController {
	
	private static final String USER_SEPARATOR = "_";
    private static final long EXPIRATION_TIME = 10 * 60 * 1000L; // 10 minutos

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserDTO user)
    {
        logger.info("createDedication init");
        user.setId(null);

        User requestData = userService.saveUser(user);
        logger.info("createDedication end");
        if(requestData!= null) {
            return ResponseEntity.ok("Created with id [" + requestData.getId() + "]");
        }
        else{
            return ResponseEntity.badRequest().body("Code company " + user.getCompanyCode() +
                   " not exists");
        }

    }

    @PutMapping()
    public ResponseEntity<String> updateUser(@RequestBody UserDTO user)
    {
        logger.info("updateUser init");
        User requestData = userService.getUserById(user.getId());
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Company comp = companyService.getCompanyByCode(user.getCompanyCode());
            if(comp == null)
            {
                return ResponseEntity.badRequest().body("Code company " + user.getCompanyCode() + " not exists");
            }

            requestData.setId(user.getId());
            requestData.setActive(user.getActive());
            requestData.setName(user.getName());
            requestData.setCode(user.getCode());
            requestData.setPassword(user.getPassword());
            requestData.setAdmin(user.getAdmin());

            requestData.setCompany(comp);
            userService.saveUser(requestData);
        }
        logger.info("updateUser end");

        return ResponseEntity.ok("Updated ok");
    }

    @GetMapping(value = "/getUserById")
    public ResponseEntity<User> getUserById(@RequestParam String id)
    {
        logger.info("getUserById init");
        User requestData = userService.getUserById(Long.parseLong(id));
        logger.info("getUserById end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestData);
    }

    @GetMapping(value = "/getUserByCode")
    public ResponseEntity<User> getUserByCode(@RequestParam String code)
    {
        logger.info("getUserByCode init");
        User requestData = userService.getUserByCode(code);
        logger.info("getUserByCode end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestData);
    }


    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id)
    {
        logger.info("deleteUser init");
        User requestData = userService.getUserById(Long.parseLong(id));
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            userService.deleteUser(requestData);
        }
        logger.info("deleteUser end");

        return ResponseEntity.ok("Deleted ok");
    }

    @PostMapping(value="/login")
    public ResponseEntity<String> loginNew(@RequestBody UserRestDTO us) {
        // Ficticio, deberá recuperarse de la BD
        // Obtener el user con el code indicado y
        // comparar si la password es la misma
        Optional<User> user = userRepository.findByCode(us.getCode());
        if(user.isPresent())
        {
            User u = user.get();
            if (u.getPassword().equals((us.getPassword())))
            {
                //user.setId(1L);
                //user.setCode(us.getCode());
                //user.setName("name");

                String token = getTokenForUser(u, true);
                return ResponseEntity.accepted().body(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

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