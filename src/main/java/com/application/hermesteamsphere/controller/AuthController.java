package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.ErrorResDTO;
import com.application.hermesteamsphere.dto.LoginReqDTO;
import com.application.hermesteamsphere.dto.LoginResDTO;
import com.application.hermesteamsphere.services.UserService;
import com.application.hermesteamsphere.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @PostMapping(value = "/login")
    @CrossOrigin
    public ResponseEntity login(@RequestBody LoginReqDTO loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();

            User user = userService.getUserByEmail(loginReq.getEmail());

            String token = jwtUtil.createToken(user);

            LoginResDTO loginRes = new LoginResDTO();
            loginRes.setEmail(email);
            loginRes.setToken(token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorResDTO errorResponse = new ErrorResDTO(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorResDTO errorResponse = new ErrorResDTO(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}