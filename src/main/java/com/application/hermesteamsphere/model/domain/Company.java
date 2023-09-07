package com.application.hermesteamsphere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "company", uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company implements Serializable
{
	// comment
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(unique = true)
	private String name;
	
	@Column(unique = true)
	@Size(max = 40)
	private String code;

	private Boolean active;
	
}
