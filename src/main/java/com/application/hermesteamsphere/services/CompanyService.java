package com.application.hermesteamsphere.services;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.dto.CompanyDTO;

public interface CompanyService
{
    Company createCompany(CompanyDTO companyDTO);

    Company getCompanyById(Long id);

    Company getCompanyByCode(String code);
}
