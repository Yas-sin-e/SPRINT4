package com.yassine.employee.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.yassine.employee.entity.Grade;
@RepositoryRestResource(path = "Grade") 
@CrossOrigin("http://localhost:4200/") //pour autoriser angular 
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Grade findByNomGraEmp(String nomGraEmp);
}
