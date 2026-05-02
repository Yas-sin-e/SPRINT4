package com.yassine.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import com.yassine.employee.entity.Employee;
import com.yassine.employee.entity.Grade;
import com.yassine.employee.entity.Image;
import org.modelmapper.ModelMapper;

@SpringBootApplication
public class EmployeeApplication implements CommandLineRunner {

    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        // Exposer les IDs dans les réponses JSON Spring Data REST
        repositoryRestConfiguration.exposeIdsFor(Employee.class, Grade.class, Image.class);
    }
}
