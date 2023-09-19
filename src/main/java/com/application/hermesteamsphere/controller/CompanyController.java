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

    @PostMapping()
    public ResponseEntity<String> createCompany(@RequestBody CompanyDTO company)
    {
        logger.info("createCompany init");
        company.setId(null);

        if(companyService.getCompanyByCode(company.getCode()) != null)
        {
            return ResponseEntity.badRequest().body("Code " + company.getCode() + " duplicated");
        }

        Company requestData = companyService.saveCompany(company);
        logger.info("createCompany end");

        return ResponseEntity.ok("Created with id [" + requestData.getId() + "]");
    }

    @PutMapping()
    public ResponseEntity<String> updateCompany(@RequestBody CompanyDTO company)
    {
        logger.info("updateCompany init");
        Company requestData = companyService.getCompanyById(company.getId());
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Company comp = companyService.getCompanyByCode(company.getCode());
            if(comp != null && comp.getId() != company.getId())
            {
                return ResponseEntity.badRequest().body("Code " + company.getCode() + " duplicated");
            }

            requestData.setId(company.getId());
            requestData.setCode(company.getCode());
            requestData.setName(company.getName());
            requestData.setActive(company.getActive());
            companyService.saveCompany(requestData);
        }
        logger.info("updateCompany end");

        return ResponseEntity.ok("Updated ok");
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


    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable String id)
    {
        logger.info("deleteCompany init");
        Company requestData = companyService.getCompanyById(Long.parseLong(id));
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            companyService.deleteCompany(requestData);
        }
        logger.info("deleteCompany end");

        return ResponseEntity.ok("Deleted ok");
    }
}
