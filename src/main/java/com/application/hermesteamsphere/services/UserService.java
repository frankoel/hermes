package com.application.hermesteamsphere.services;

import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.UserDTO;

import java.util.List;

public interface UserService
{
    User saveUser(UserDTO userDTO);

    User saveUser(User user);

    User getUserById(Long id);

    User getUserByCode(String code);
    
    List<User> getUsersByCodeCompany(String codeCompany);

    void deleteUser(User project);

    UserDTO toDTO(User user);
}
