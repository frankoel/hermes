package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.data.Dedication;
import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.UserDTO;
import com.application.hermesteamsphere.repositories.UserRepository;
import com.application.hermesteamsphere.services.CompanyService;
import com.application.hermesteamsphere.services.DedicationService;
import com.application.hermesteamsphere.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @Autowired
    DedicationService dedicationService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping("/user")
    @CrossOrigin
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

    @PutMapping("/user")
    @CrossOrigin
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
            requestData.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            requestData.setEmail(user.getEmail());
            requestData.setAdmin(user.getAdmin());

            requestData.setCompany(comp);
            userService.saveUser(requestData);
        }
        logger.info("updateUser end");

        return ResponseEntity.ok("Updated ok");
    }

    @GetMapping(value = "/user/getUserById")
    @CrossOrigin
    public ResponseEntity<UserDTO> getUserById(@RequestParam String id)
    {
        logger.info("getUserById init");
        User requestData = userService.getUserById(Long.parseLong(id));
        logger.info("getUserById end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.toDTO(requestData));
    }

    @GetMapping(value = "/user/getUserByCode")
    @CrossOrigin
    public ResponseEntity<UserDTO> getUserByCode(@RequestParam String code)
    {
        logger.info("getUserByCode init");
        User requestData = userService.getUserByCode(code);
        logger.info("getUserByCode end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.toDTO(requestData));
    }

    @GetMapping(value = "/user/getUsersByCodeCompany")
    @CrossOrigin
    public ResponseEntity<List<UserDTO>> getUsersByCodeCompany(@RequestParam String codeCompany)
    {
        logger.info("getUserByCodeCompany init");
        List<User> requestData = userService.getUsersByCodeCompany(codeCompany);
        logger.info("getUserByCodeCompany end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.toListDTO(requestData));
    }


    @DeleteMapping(value="/user/{id}")
    @CrossOrigin
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
            List<Dedication> dedications = dedicationService.getDedicationsByCodeUser(requestData.getCode());
            if (dedications == null || dedications.isEmpty())
            {
                userService.deleteUser(requestData);
            }
            else
            {
                return ResponseEntity.badRequest().build();
            }

        }
        logger.info("deleteUser end");

        return ResponseEntity.ok("Deleted ok");
    }

}