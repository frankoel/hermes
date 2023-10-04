package com.application.hermesteamsphere.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DedicationDTO
{
    private Long id;
	private Date hoursInit;
	private Date hoursEnd;
	private String projectCode;
	private String user;
	private String description;
}
