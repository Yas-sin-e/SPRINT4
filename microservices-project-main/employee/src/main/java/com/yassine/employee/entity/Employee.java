package com.yassine.employee.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
	  @Id
	  @GeneratedValue(strategy=GenerationType.IDENTITY)
	  private  int   idEmploye;
	  @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
	  @ToString.Exclude
	  @EqualsAndHashCode.Exclude
	  private List<Image> images;

	  private String nomEmploye;
	  private String prenomEmploye;
	  private String posteEmploye ;
	  private Date dateEmbauche ;
	  private int salaire;
	  private  String  email;
	  private  String  telephone;
	  private  String  adresse;

		private String imagePath;
	  
	  @ManyToOne //plusieur employe a le meme garde
	  @JoinColumn(name = "idGraEmp")
	  private Grade grade ;



	  @Override
	  public String toString() {
		return "Employee [idEmploye=" + idEmploye + ", nomEmploye=" + nomEmploye + ", prenomEmploye=" + prenomEmploye
				+ ", posteEmploye=" + posteEmploye + ", dateEmbauche=" + dateEmbauche + ", salaire=" + salaire
				+ ", email=" + email + ", telephone=" + telephone + ", adresse=" + adresse +"]";
	  }	
	  
	  
	  
}
