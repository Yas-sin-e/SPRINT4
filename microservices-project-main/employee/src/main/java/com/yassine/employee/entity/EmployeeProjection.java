package com.yassine.employee.entity;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "nomEmploye", types = { Employee.class }) 
public interface EmployeeProjection {
		public String getNomEmploye();
		
}
