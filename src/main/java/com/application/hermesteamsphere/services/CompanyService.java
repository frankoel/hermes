package com.application.hermesteamsphere.services;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.dto.CompanyDTO;

public interface CompanyService
{
    Company saveCompany(CompanyDTO companyDTO);

    Company saveCompany(Company company);

    Company getCompanyById(Long id);

    Company getCompanyByCode(String code);

    void deleteCompany(Company company);
}
