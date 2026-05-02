package com.yassine.employee.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Entity
public class Grade {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private  int idGraEmp;   
		
		private   String nomGraEmp;     
		private String niveau ;
		
		@JsonIgnore 
		@OneToMany(mappedBy="grade")
		private List<Employee> employees;
		
		
		public List<Employee> getEmployees() {
			return employees;
		}

		public void setEmployees(List<Employee> employees) {
			this.employees = employees;
		}

		
		
		
		
		
}

