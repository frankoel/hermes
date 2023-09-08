package com.application.hermesteamsphere.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CompanyDTO
{
    private Long id;
	private String name;
	private String code;
	private Boolean active;
}
