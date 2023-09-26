package com.application.hermesteamsphere.services;

import com.application.hermesteamsphere.data.Project;
import com.application.hermesteamsphere.dto.ProjectDTO;

import java.util.List;

public interface ProjectService
{
    Project saveProject(ProjectDTO projectDTO);

    Project saveProject(Project project);

    Project getProjectById(Long id);

    Project getProjectByCode(String code);
    
    List<Project> getProjectsByCodeCompany(String codeCompany);

    void deleteProject(Project project);
}
