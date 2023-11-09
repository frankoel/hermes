package com.application.hermesteamsphere.services;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.dto.CompanyDTO;

import java.util.List;

public interface CompanyService
{
    Company saveCompany(CompanyDTO companyDTO);

    Company saveCompany(Company company);

    List<Company> getAllCompany();

    Company getCompanyById(Long id);

    Company getCompanyByCode(String code);

    Company getCompanyByName(String name);

    void deleteCompany(Company company);

    CompanyDTO toDTO(Company company);

    List<CompanyDTO> toListDTO(List<Company> listCompany);
}
