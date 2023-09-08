package com.application.hermesteamsphere.services.impl;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.dto.CompanyDTO;
import com.application.hermesteamsphere.repositories.CompanyRepository;
import com.application.hermesteamsphere.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService
{

    @Autowired
    CompanyRepository companyRepository;


    public Company createCompany(CompanyDTO companyDTO)
    {
        return companyRepository.save(companyDTOtoCompany(companyDTO));
    }

    @Override
    public Company getCompanyById(Long id)
    {
        Optional<Company> result = companyRepository.findById(id);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }

    @Override
    public Company getCompanyByCode(String code)
    {
        Optional<Company> result = companyRepository.findByCode(code);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }

    private Company companyDTOtoCompany(CompanyDTO companyDTO)
    {
        Company company = new Company();

        company.setId(companyDTO.getId());
        company.setActive(companyDTO.getActive());
        company.setName(companyDTO.getName());
        company.setCode(companyDTO.getCode());

        return company;
    }
}
