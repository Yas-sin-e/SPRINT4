package com.yassine.employee.restcontrollers;

import java.util.List;

import com.yassine.employee.dto.EmployeeDTO;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yassine.employee.entity.Employee;
import com.yassine.employee.service.EmployeeService;

import ch.qos.logback.core.joran.spi.HttpUtil.RequestMethod;

@RestController //trasfomer les retoure en json pret pour angular 
@RequestMapping("/api") //le prefixe commun pour tous les routes
public class EmployeeRESTController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/auth")
    Authentication getAuth(Authentication auth)
    {
        return auth;
    }
    @GetMapping("/all")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/getbyid/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping("/addemp")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO emp) {
        return employeeService.saveEmployee(emp);
    }

    @PutMapping("/updateemp")
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO emp) {
        return employeeService.updateEmployee(emp);
    }

    @DeleteMapping("/delemp/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
    }

    @GetMapping("/EmployeeGrade/{idGradeEmp}")
    public List<EmployeeDTO> getEmployeesByGrade(@PathVariable Integer idGradeEmp) {
        return employeeService.findByGradeIdGraEmp(idGradeEmp);
    }
}

