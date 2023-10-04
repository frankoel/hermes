package com.application.hermesteamsphere.repositories;

import com.application.hermesteamsphere.data.Dedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DedicationRepository extends JpaRepository<Dedication, Long>
{
    List<Dedication> findByUserCode(String codeUser);
    List<Dedication> findByProjectCode(String codeProject);

    List<Dedication> findByProjectCodeAndUserCode(String codeProject, String codeUser);
}
