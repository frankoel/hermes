package com.application.hermesteamsphere.security;

import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
        {
            List<String> roles = new ArrayList<>();
            if (user.get().getAdmin().booleanValue())
            {
                roles.add("ADMIN");
            }
            else
            {
                roles.add("USER");
            }
            logger.info("Creando userDetails");
            userDetails =
                    org.springframework.security.core.userdetails.User.builder()
                            .username(user.get().getEmail())
                            .password(user.get().getPassword())
                            .roles(roles.toArray(new String[1]))
                            .build();
            return userDetails;
        }
        logger.info("userDetails null");
        return userDetails;
    }
}