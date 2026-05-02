package com.yassine.employee.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yassine.employee.entity.Employee;
import com.yassine.employee.entity.Grade;

@RepositoryRestResource(path = "rest") //optianal
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	List<Employee> findByNomEmployeContainingIgnoreCase(String nom);
	
	List<Employee> findByNomEmploye(String nom);
	
	@Query("select e from Employee e where e.nomEmploye like %:nom  and e.salaire > :salaire") 
	List<Employee> findBynometsalaire (@Param("nom")String nom,@Param("salaire")int salaire ); 

	@Query("select e from Employee e where e.grade = ?1") 
	List<Employee> findByGrade (Grade grade); 
	List<Employee> findByGradeIdGraEmp(int id); 
	
	List<Employee> findByOrderByNomEmployeAsc(); 
	List<Employee> findByOrderByNomEmployeDesc(); 
	
	@Query("select e from Employee e order by e.nomEmploye ASC, e.salaire DESC") 
	List<Employee> trierEmployeesNomsSalaire ();
	
	
	
}
