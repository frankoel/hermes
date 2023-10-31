package com.application.hermesteamsphere.controller;

import com.application.hermesteamsphere.data.Dedication;
import com.application.hermesteamsphere.data.Project;
import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.DedicationDTO;
import com.application.hermesteamsphere.services.DedicationService;
import com.application.hermesteamsphere.services.ProjectService;
import com.application.hermesteamsphere.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dedication")
public class DedicationController
{
    @Autowired
    DedicationService dedicationService;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    private static final Logger logger = LoggerFactory.getLogger(DedicationController.class);

    @PostMapping()
    public ResponseEntity<String> createDedication(@RequestBody DedicationDTO dedication)
    {
        logger.info("createDedication init");
        dedication.setId(null);

        Dedication requestData = dedicationService.saveDedication(dedication);
        logger.info("createDedication end");
        if(requestData!= null) {
            return ResponseEntity.ok("Created with id [" + requestData.getId() + "]");
        }
        else{
            return ResponseEntity.badRequest().body("Code user " + dedication.getUser() +
                    " or code project" + dedication.getProjectCode()+ " not exists");
        }

    }

    @PutMapping()
    public ResponseEntity<String> updateDedication(@RequestBody DedicationDTO dedication)
    {
        logger.info("updateDedication init");
        Dedication requestData = dedicationService.getDedicationById(dedication.getId());
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            User user = userService.getUserByCode(dedication.getUser());
            if(user == null)
            {
                return ResponseEntity.badRequest().body("Code user " + dedication.getUser() + " not exists");
            }

            Project project = projectService.getProjectByCode(dedication.getProjectCode());
            if(project == null)
            {
                return ResponseEntity.badRequest().body("Code project " + dedication.getProjectCode() + " not exists");
            }

            requestData.setId(dedication.getId());
            requestData.setDescription(dedication.getDescription());
            requestData.setHoursInit(dedication.getHoursInit());
            requestData.setHoursEnd(dedication.getHoursEnd());
            requestData.setUser(user);
            requestData.setProject(project);
            dedicationService.saveDedication(requestData);
        }
        logger.info("updateDedication end");

        return ResponseEntity.ok("Updated ok");
    }

    @GetMapping(value = "/getDedicationById")
    public ResponseEntity<DedicationDTO> getDedicationById(@RequestParam String id)
    {
        logger.info("getDedicationById init");
        Dedication requestData = dedicationService.getDedicationById(Long.parseLong(id));
        logger.info("getDedicationById end");

        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dedicationService.toDTO(requestData));
    }

    @GetMapping(value = "/getDedicationByProjectAndUser")
    public ResponseEntity<List<DedicationDTO>> getDedicationByProjectAndUser(@RequestParam String codProject, @RequestParam String codUser)
    {
        logger.info("getDedicationById init");
        List<Dedication> requestData = dedicationService.getDedicationsByCodeProjectAndCodeUser(codProject, codUser);
        logger.info("getDedicationById end");

        if(requestData == null || requestData.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dedicationService.toListDTO(requestData));
    }


    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteDedication(@PathVariable String id)
    {
        logger.info("deleteDedication init");
        Dedication requestData = dedicationService.getDedicationById(Long.parseLong(id));
        if(requestData == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            dedicationService.deleteDedication(requestData);
        }
        logger.info("deleteDedication end");

        return ResponseEntity.ok("Deleted ok");
    }
}
