package com.yassine.employee.service;

import java.util.List;

import com.yassine.employee.dto.EmployeeDTO;
import com.yassine.employee.entity.Employee;
import com.yassine.employee.entity.Grade;

public interface EmployeeService {

	EmployeeDTO saveEmployee(EmployeeDTO e);
	EmployeeDTO updateEmployee(EmployeeDTO e);
	Employee saveEmploye(Employee e);
	void deleteEmployeeById(int id);

	EmployeeDTO getEmployee(int id);
	Employee getEmploye(int id);
	List<EmployeeDTO> getAllEmployees();

	List<EmployeeDTO> chercherparnomEmployee(String nom);
	List<EmployeeDTO> chercherparnomEmployeeContains(String nom);
	List<EmployeeDTO> findBynometsalaire(String nom, int salaire);

	List<EmployeeDTO> findByGrade(Grade grade);
	List<EmployeeDTO> findByGradeIdGraEmp(int id);

	List<EmployeeDTO> findByOrderByNomEmployeAsc();
	List<EmployeeDTO> trierEmployeesNomsSalaire();

	EmployeeDTO convertEntityToDto(Employee e);
	Employee convertDtoToEntity(EmployeeDTO dto);
}