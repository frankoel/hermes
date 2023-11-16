package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.data.Dedication;
import com.application.hermesteamsphere.data.Project;
import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.CompanyDTO;
import com.application.hermesteamsphere.services.CompanyService;
import com.application.hermesteamsphere.services.ProjectService;
import com.application.hermesteamsphere.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController
{
    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<String> createCompany(@RequestBody CompanyDTO company)
    {
        logger.info("createCompany init");
        company.setId(null);

        if(companyService.getCompanyByCode(company.getCode()) != null)
        {
            return ResponseEntity.badRequest().body("Company code " + company.getCode() + " is duplicated");
        }

        if(companyService.getCompanyByName(company.getName()) != null)
        {
            return ResponseEntity.badRequest().body("Company name " + company.getName() + " is duplicated");
        }

        Company requestData = companyService.saveCompany(company);
        logger.info("createCompany end");

        return ResponseEntity.ok("Created with id [" + requestData.getId() + "]");
    }
    @CrossOrigin
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
                return ResponseEntity.badRequest().body("Company code" + company.getCode() + " is duplicated");
            }

            comp = companyService.getCompanyByName(company.getName());
            if(comp != null && comp.getId() != company.getId())
            {
                return ResponseEntity.badRequest().body("Company name" + company.getName() + " is duplicated");
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
    @CrossOrigin
    @GetMapping(value = "/getAllCompany")
    public ResponseEntity<List<CompanyDTO>> getAllCompany()
    {
        logger.info("getAllCompany init");
        List<Company> requestData = companyService.getAllCompany();
        logger.info("getAllCompany end");

        if(requestData == null || requestData.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyService.toListDTO(requestData));
    }
    @CrossOrigin
    @GetMapping(value = "/getCompanyById")
    public ResponseEntity<CompanyDTO> getCompanyById(@RequestParam String id)
    {
        logger.info("getCompanyById init");
        Company requestData = companyService.getCompanyById(Long.parseLong(id));
        logger.info("getCompanyById end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyService.toDTO(requestData));
    }
    @CrossOrigin
    @GetMapping(value = "/getCompanyByCode")
    public ResponseEntity<CompanyDTO> getCompanyByCode(@RequestParam String code)
    {
        logger.info("getCompanyByCode init");
        Company requestData = companyService.getCompanyByCode(code);
        logger.info("getCompanyByCode end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(companyService.toDTO(requestData));
    }

    @CrossOrigin
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
            List<User> users = userService.getUsersByCodeCompany(requestData.getCode());
            List<Project> projects = projectService.getProjectsByCodeCompany(requestData.getCode());
            if ((users == null || users.isEmpty()) && (projects == null || projects.isEmpty()))
            {
                companyService.deleteCompany(requestData);
            }
            else
            {
                return ResponseEntity.badRequest().build();
            }

        }

        logger.info("deleteCompany end");

        return ResponseEntity.ok("Deleted ok");
    }
}
