package com.application.hermesteamsphere.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dedication")
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Dedication implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "hours_init")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyyMMddHHmm")
	private Date hoursInit;

	@Column(name = "hours_end")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyyMMddHHmm")
	private Date hoursEnd;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	private String description;
	
}
