package com.application.hermesteamsphere.repositories;

import com.application.hermesteamsphere.data.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>
{
    Optional<Project> findByCode(String code);

    List<Project> findByCompanyCode(String codeCompany);
}
