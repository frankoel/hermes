package com.application.hermesteamsphere.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String code;
    private String password;
	private String companyCode;
    private Boolean active;
    private Boolean admin;
}
