package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.data.Project;
import com.application.hermesteamsphere.dto.CompanyDTO;
import com.application.hermesteamsphere.dto.ProjectDTO;
import com.application.hermesteamsphere.services.CompanyService;
import com.application.hermesteamsphere.services.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController
{
    @Autowired
    ProjectService projectService;

    @Autowired
    CompanyService companyService;

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @PostMapping()
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO project)
    {
        logger.info("createProject init");
        project.setId(null);

        if(projectService.getProjectByCode(project.getCode()) != null)
        {
            return ResponseEntity.badRequest().body("Code " + project.getCode() + " duplicated");
        }

        Project requestData = projectService.saveProject(project);
        logger.info("createProject end");
        if(requestData!= null) {
            return ResponseEntity.ok("Created with id [" + requestData.getId() + "]");
        }
        else{
            return ResponseEntity.badRequest().body("Code company " + project.getCodeCompany() + " not exists");
        }

    }

    @PutMapping()
    public ResponseEntity<String> updateProject(@RequestBody ProjectDTO project)
    {
        logger.info("updateProject init");
        Project requestData = projectService.getProjectById(project.getId());
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Project proy = projectService.getProjectByCode(project.getCode());
            if(proy != null && proy.getId() != project.getId())
            {
                return ResponseEntity.badRequest().body("Code " + project.getCode() + " duplicated");
            }

            Company comp = companyService.getCompanyByCode(project.getCodeCompany());
            if(comp == null)
            {
                return ResponseEntity.badRequest().body("Code company " + project.getCodeCompany() + " not exists");
            }

            requestData.setId(project.getId());
            requestData.setCode(project.getCode());
            requestData.setName(project.getName());
            requestData.setActive(project.getActive());
            requestData.setCompany(comp);
            projectService.saveProject(requestData);
        }
        logger.info("updateProject end");

        return ResponseEntity.ok("Updated ok");
    }

    @GetMapping(value = "/getProjectById")
    public ResponseEntity<Project> getProjectById(@RequestParam String id)
    {
        logger.info("getProjectById init");
        Project requestData = projectService.getProjectById(Long.parseLong(id));
        logger.info("getProjectById end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestData);
    }

    @GetMapping(value = "/getProjectByCode")
    public ResponseEntity<Project> getProjectByCode(@RequestParam String code)
    {
        logger.info("getProjectByCode init");
        Project requestData = projectService.getProjectByCode(code);
        logger.info("getProjectByCode end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestData);
    }


    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable String id)
    {
        logger.info("deleteProject init");
        Project requestData = projectService.getProjectById(Long.parseLong(id));
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            projectService.deleteProject(requestData);
        }
        logger.info("deleteProject end");

        return ResponseEntity.ok("Deleted ok");
    }
}