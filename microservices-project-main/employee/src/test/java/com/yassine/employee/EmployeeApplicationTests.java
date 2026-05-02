package com.yassine.employee;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.yassine.employee.Repository.EmployeeRepository;
import com.yassine.employee.Repository.GradeRepository;
import com.yassine.employee.entity.Employee;
import com.yassine.employee.entity.Grade;

@SpringBootTest
class EmployeeApplicationTests {
	@Autowired
	private  EmployeeRepository employeeRepository;
	@Autowired
	private  GradeRepository gradeRepository;
	
	
	@Test
	public void ajouterEmployee() {
		Grade newGrade=new Grade();
		newGrade.setNomGraEmp("Chef de Projet");
        newGrade.setNiveau("Expert");
        
        Grade savedGrade = gradeRepository.save(newGrade);
		Employee E =new Employee();
		E.setNomEmploye("yassine");
        E.setPrenomEmploye("aounallah");
        E.setPosteEmploye("UX expert");
        E.setDateEmbauche(new Date()); 
        E.setSalaire(3200);
        E.setEmail("y.a@test.com");
        E.setTelephone("11223344");
        E.setAdresse("Tunis, Nabeul");
		
		this.employeeRepository.save(E);
		System.out.println("Employé Sophie Dubois sauvegardé avec succès avec le Grade ID: " + savedGrade.getIdGraEmp());
		
	}
	@Test
	public void testfindemployee() {
		employeeRepository.findById(1).ifPresent(emp -> {
			System.out.println("le employee est :"+emp);
		});
		
	}
	@Test
	public void Updateemployee() {
		employeeRepository.findById(4).ifPresent(emp -> {
			emp.setSalaire(60);
			employeeRepository.save(emp);
			System.out.println(emp);
		});
	}
	@Test
	public void deleteteemployee() {
		employeeRepository.deleteById(1);
		
	}
	@Test
	public void listerEmployee() {
		List<Employee> list_emp=employeeRepository.findAll();
		for(Employee e : list_emp) {
			System.out.println(e);
		}
	}
	@Test
	public void chercherparnomEmployee() {
		List<Employee> list_emp=employeeRepository.findByNomEmploye("yassine");
		for(Employee e : list_emp) {
			System.out.println(e);
		}
	}
	
	@Test
	public void chercherparnomEmployeecontains() {
		List<Employee> list_emp=employeeRepository.findByNomEmployeContainingIgnoreCase("lolo");
		for(Employee e : list_emp) {
			System.out.println(e);
		}
	}
	
	@Test
	public void testfindbysalireetnom() 
	{ 
	List<Employee>  list_emp = employeeRepository.findBynometsalaire("lolo",3000); 
	for (Employee e : list_emp) 
	{ 
	System.out.println(e); 
	} }
	
	@Test
	public void testfindByGrade() { 
	Grade g = new Grade(); 
	    g.setIdGraEmp(1);
	    System.out.println(g);
	List<Employee>  list_emp = employeeRepository.findByGrade(g); 
	for (Employee e : list_emp) 
	{ 
	System.out.println(e); 
	}} 
	
	
	@Test
	public void findbyidgrade () 
	{    
	List<Employee>  list_emp = employeeRepository.findByGradeIdGraEmp(1);

	for (Employee e : list_emp) 
	{ 
	System.out.println(e); 
	} 
	}
	@Test
	public void testerfindByOrderNomEmployeAsc () 
	{    
	List<Employee>  list_emp = employeeRepository.findByOrderByNomEmployeAsc();

	for (Employee e : list_emp) 
	{ 
	System.out.println(e); 
	} }
	@Test
	public void testerfindByOrderNomEmployeDesc () 
	{    
	List<Employee>  list_emp = employeeRepository.findByOrderByNomEmployeDesc();

	for (Employee e : list_emp) 
	{ 
	System.out.println(e); 
	}
	}
	@Test
	public void testtrierEmployeesNomsSalaire () 
	{    
	List<Employee>  list_emp = employeeRepository.trierEmployeesNomsSalaire();

	for (Employee e : list_emp) 
	{ 
	System.out.println(e); 
	}
	
	} 
	 

}
