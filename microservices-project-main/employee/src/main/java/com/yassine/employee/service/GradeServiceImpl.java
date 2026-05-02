package com.yassine.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yassine.employee.Repository.EmployeeRepository;
import com.yassine.employee.Repository.GradeRepository;
import com.yassine.employee.entity.Employee;
import com.yassine.employee.entity.Grade;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Grade saveGrade(Grade grade) {
       
        return gradeRepository.save(grade);
    }

    @Override
    public Grade updateGrade(Integer id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade non trouvé avec id: " + id));

        grade.setNomGraEmp(gradeDetails.getNomGraEmp());
        grade.setNiveau(gradeDetails.getNiveau());
        return gradeRepository.save(grade);
    }

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public Grade getGradeById(Integer id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade non trouvé avec id: " + id));
    }

    @Override
    public void deleteGrade(Integer id) {
    	 List<Employee> empWithGrade = employeeRepository.findByGradeIdGraEmp(id);
    	    for(Employee e : empWithGrade) {
    	        e.setGrade(null);
    	        employeeRepository.save(e);
    	    }
    	    gradeRepository.deleteById(id);
    }
}
