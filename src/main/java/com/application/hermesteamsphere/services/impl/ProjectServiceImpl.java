package com.application.hermesteamsphere.services.impl;

import com.application.hermesteamsphere.data.Company;
import com.application.hermesteamsphere.data.Project;
import com.application.hermesteamsphere.dto.ProjectDTO;
import com.application.hermesteamsphere.repositories.ProjectRepository;
import com.application.hermesteamsphere.services.CompanyService;
import com.application.hermesteamsphere.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService
{

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CompanyService companyService;

    @Override
    public Project saveProject(ProjectDTO projectDTO)
    {
        Project project = projectDTOtoProject(projectDTO);
        if(project!=null) {
            return projectRepository.save(project);
        }
        return null;
    }

    @Override
    public Project saveProject(Project project)
    {
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Project project)
    {
        projectRepository.delete(project);
    }

    @Override
    public Project getProjectById(Long id)
    {
        Optional<Project> result = projectRepository.findById(id);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }

    @Override
    public Project getProjectByCode(String code)
    {
        Optional<Project> result = projectRepository.findByCode(code);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }

    @Override
    public Project getProjectByName(String name)
    {
        Optional<Project> result = projectRepository.findByName(name);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }

    @Override
    public List<Project> getProjectsByCodeCompany(String codeCompany)
    {
        return projectRepository.findByCompanyCode(codeCompany);
    }

    private Project projectDTOtoProject(ProjectDTO projectDTO)
    {
        Project project = new Project();

        project.setId(projectDTO.getId());
        project.setActive(projectDTO.getActive());
        project.setName(projectDTO.getName());
        project.setCode(projectDTO.getCode());
        project.setDescription(projectDTO.getDescription());

        Company company = companyService.getCompanyByCode(projectDTO.getCodeCompany());
        if(company != null)
        {
            project.setCompany(company);
        }
        else
        {
            return null;
        }

        return project;
    }

    @Override
    public ProjectDTO toDTO(Project project)
    {
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setId(project.getId());
        projectDTO.setActive(project.getActive());
        projectDTO.setName(project.getName());
        projectDTO.setCode(project.getCode());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setCodeCompany(project.getCompany().getCode());

        return projectDTO;
    }

    public List<ProjectDTO> toListDTO(List<Project> listProject)
    {
        List<ProjectDTO> listProjectDTO = new ArrayList<>();
        for (Project project:listProject)
        {
            listProjectDTO.add(toDTO(project));
        }
        return listProjectDTO;
    }


}
