package com.application.hermesteamsphere.repositories;

import com.application.hermesteamsphere.data.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>
{
    Optional<Company> findByCode(String code);

    Optional<Company> findByName(String name);
}
