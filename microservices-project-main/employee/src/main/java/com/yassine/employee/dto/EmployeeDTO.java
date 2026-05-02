package com.yassine.employee.dto;

import java.util.Date;
import java.util.List;

import com.yassine.employee.entity.Grade;
import com.yassine.employee.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private int idEmploye;
    private String nomEmploye;
    private String prenomEmploye;
    private String posteEmploye;
    private Date dateEmbauche;
    private int salaire;
    private String email;
    private String telephone;
    private String adresse;
    private Grade grade;
    // au lieu de retourner tout Grade
    private String nomGrade;
    private List<Image> images;
    private String imagePath;
}
