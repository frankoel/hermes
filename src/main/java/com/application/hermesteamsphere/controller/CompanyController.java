package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.dto.CompanyDTO;
import com.application.hermesteamsphere.services.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController
{
    @Autowired
    CompanyService companyService;

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @PostMapping(value = "/")
    public ResponseEntity<Company> createCompany(@RequestBody CompanyDTO company)
    {
        logger.info("createCompany init");
        Company requestData = companyService.createCompany(company);
        logger.info("createCompany end");

        return ResponseEntity.ok(requestData);
    }

    @GetMapping(value = "/getCompanyById")
    public ResponseEntity<Company> getCompanyById(@RequestParam String id)
    {
        logger.info("getCompanyById init");
        Company requestData = companyService.getCompanyById(Long.parseLong(id));
        logger.info("getCompanyById end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestData);
    }

    @GetMapping(value = "/getCompanyByCode")
    public ResponseEntity<Company> getCompanyByCode(@RequestParam String code)
    {
        logger.info("getCompanyByCode init");
        Company requestData = companyService.getCompanyByCode(code);
        logger.info("getCompanyByCode end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestData);
    }
}
