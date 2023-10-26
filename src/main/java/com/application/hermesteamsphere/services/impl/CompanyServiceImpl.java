package com.application.hermesteamsphere.services.impl;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.dto.CompanyDTO;
import com.application.hermesteamsphere.repositories.CompanyRepository;
import com.application.hermesteamsphere.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService
{

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Company saveCompany(CompanyDTO companyDTO)
    {
        return companyRepository.save(companyDTOtoCompany(companyDTO));
    }

    @Override
    public Company saveCompany(Company company)
    {
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompany(Company company)
    {
        companyRepository.delete(company);
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

    @Override
    public CompanyDTO toDTO(Company company)
    {
        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setId(companyDTO.getId());
        companyDTO.setActive(companyDTO.getActive());
        companyDTO.setName(companyDTO.getName());
        companyDTO.setCode(companyDTO.getCode());

        return companyDTO;
    }
}
