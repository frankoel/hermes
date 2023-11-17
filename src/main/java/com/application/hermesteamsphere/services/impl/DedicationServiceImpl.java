package com.application.hermesteamsphere.services.impl;

import com.application.hermesteamsphere.data.Dedication;
import com.application.hermesteamsphere.data.Project;
import com.application.hermesteamsphere.data.User;
import com.application.hermesteamsphere.dto.DedicationDTO;
import com.application.hermesteamsphere.repositories.DedicationRepository;
import com.application.hermesteamsphere.services.DedicationService;
import com.application.hermesteamsphere.services.ProjectService;
import com.application.hermesteamsphere.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DedicationServiceImpl implements DedicationService
{

    @Autowired
    DedicationRepository dedicationRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Override
    public Dedication saveDedication(DedicationDTO dedicationDTO)
    {
        Dedication dedication = dedicationDTOtoDedication(dedicationDTO);
        if(dedication!=null) {
            return dedicationRepository.save(dedication);
        }
        return null;
    }

    @Override
    public Dedication saveDedication(Dedication dedication)
    {
        return dedicationRepository.save(dedication);
    }

    @Override
    public void deleteDedication(Dedication dedication)
    {
        dedicationRepository.delete(dedication);
    }

    @Override
    public Dedication getDedicationById(Long id)
    {
        Optional<Dedication> result = dedicationRepository.findById(id);
        if(result.isPresent())
        {
            return result.get();
        }

        return null;
    }


    @Override
    public List<Dedication> getDedicationsByCodeProject(String codeProject)
    {
        return dedicationRepository.findByProjectCode(codeProject);

    }

    @Override
    public List<Dedication> getDedicationsByCodeUser(String codeUser)
    {
        return dedicationRepository.findByUserCode(codeUser);

    }

    @Override
    public List<Dedication> getDedicationsByCodeProjectAndCodeUser(String codeProject, String codeUser)
    {
        return dedicationRepository.findByProjectCodeAndUserCode(codeProject, codeUser);
    }

    private Dedication dedicationDTOtoDedication(DedicationDTO dedicationDTO)
    {
        Dedication dedication = new Dedication();

        dedication.setId(dedicationDTO.getId());
        dedication.setHoursInit(dedicationDTO.getHoursInit());
        dedication.setHoursEnd(dedicationDTO.getHoursEnd());
        dedication.setDescription(dedicationDTO.getDescription());

        Project project = projectService.getProjectByCode(dedicationDTO.getProjectCode());
        if(project != null)
        {
            dedication.setProject(project);
        }
        else
        {
            return null;
        }

        User user = userService.getUserByCode(dedicationDTO.getUser());
        if(user != null)
        {
            dedication.setUser(user);
        }
        else
        {
            return null;
        }

        return dedication;
    }
    @Override
    public DedicationDTO toDTO(Dedication dedication)
    {
        DedicationDTO dedicationDTO = new DedicationDTO();

        dedicationDTO.setId(dedication.getId());
        dedicationDTO.setHoursInit(dedication.getHoursInit());
        dedicationDTO.setHoursEnd(dedication.getHoursEnd());
        dedicationDTO.setDescription(dedication.getDescription());
        dedicationDTO.setUser(dedication.getUser().getCode());
        dedicationDTO.setProjectCode(dedication.getProject().getCode());

        return dedicationDTO;
    }
    @Override
    public List<DedicationDTO> toListDTO(List<Dedication> listDedication)
    {
        List<DedicationDTO> listDedicationDTO = new ArrayList<>();
        for (Dedication dedication:listDedication)
        {
            listDedicationDTO.add(toDTO(dedication));
        }
        return listDedicationDTO;
    }
}
