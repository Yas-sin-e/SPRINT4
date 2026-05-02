package com.yassine.employee.Repository;

import com.yassine.employee.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByEmployeeIdEmploye(int idEmploye);
}
