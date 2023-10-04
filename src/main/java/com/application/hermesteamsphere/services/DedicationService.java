package com.application.hermesteamsphere.services;

import com.application.hermesteamsphere.data.Dedication;
import com.application.hermesteamsphere.data.Project;
import com.application.hermesteamsphere.dto.DedicationDTO;

import java.util.List;

public interface DedicationService
{
    Dedication saveDedication(DedicationDTO dedicationDTO);

    Dedication saveDedication(Dedication dedication);

    Dedication getDedicationById(Long id);
    
    List<Dedication> getDedicationsByCodeProject(String codeProject);

    List<Dedication> getDedicationsByCodeProjectAndCodeUser(String codeProject, String codeUser);

    void deleteDedication(Dedication dedication);
}