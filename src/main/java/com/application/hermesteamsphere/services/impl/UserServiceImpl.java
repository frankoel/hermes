package com.application.hermesteamsphere.services.impl;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.UserDTO;
import com.application.hermesteamsphere.repositories.UserRepository;
import com.application.hermesteamsphere.services.CompanyService;
import com.application.hermesteamsphere.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyService companyService;

    @Override
    public User saveUser(UserDTO userDTO)
    {
        User user = userDTOtoUser(userDTO);
        if(user!=null) {
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User saveUser(User user)
    {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user)
    {
        userRepository.delete(user);
    }

    @Override
    public User getUserById(Long id)
    {
        Optional<User> result = userRepository.findById(id);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }

    @Override
    public User getUserByCode(String code)
    {
        Optional<User> result = userRepository.findByCode(code);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }

    @Override
    public List<User> getUsersByCodeCompany(String codeCompany)
    {
        return userRepository.findByCompanyCode(codeCompany);
    }

    private User userDTOtoUser(UserDTO userDTO)
    {
        User user = new User();

        user.setId(userDTO.getId());
        user.setActive(userDTO.getActive());
        user.setName(userDTO.getName());
        user.setCode(userDTO.getCode());
        user.setPassword(userDTO.getPassword());
        user.setAdmin(userDTO.getAdmin());

        Company company = companyService.getCompanyByCode(userDTO.getCompanyCode());
        if(company != null)
        {
            user.setCompany(company);
        }
        else
        {
            return null;
        }

        return user;
    }

    @Override
    public UserDTO toDTO(User user)
    {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setActive(user.getActive());
        userDTO.setName(user.getName());
        userDTO.setCode(user.getCode());
        userDTO.setPassword("*************");
        userDTO.setAdmin(user.getAdmin());
        userDTO.setCompanyCode(user.getCompany().getCode());

        return userDTO;
    }
}
