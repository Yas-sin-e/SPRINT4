import { Component, OnInit } from '@angular/core';
import { Grade } from '../model/Grade.model';
import { EmpServices } from '../services/emp-services';
import { UpdateGrade } from '../update-grade/update-grade';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-liste-grade',
  standalone: true,
  imports: [UpdateGrade, CommonModule],
  templateUrl: './liste-grade.html',
})
export class ListeGrade implements OnInit {

  grades!: Grade[];
  updategrade: Grade = { idGraEmp: null, nomGraEmp: "", niveau: "" };
  ajout = true;

  constructor(private employeservice: EmpServices) {}

  ngOnInit(): void {
    this.chargergrades();
  }

  // Charger tous les grades depuis le service
  chargergrades() {
    this.employeservice.listegrades().subscribe(g => this.grades = g);
  }

  // Méthode appelée après ajout ou édition d'un grade
  gradeUpdated(gra: Grade): void {
    if (this.ajout) {
      this.employeservice.ajouterGrade(gra).subscribe(() => this.chargergrades());
    } else {
      this.employeservice.updateGrade(gra).subscribe(() => this.chargergrades());
    }

    this.ajout = true;
    this.updategrade = { idGraEmp: null, nomGraEmp: "", niveau: "" };
  }

  // Préparer le grade pour édition
  update_grade(gra: Grade) {
    this.updategrade = { ...gra };
    this.ajout = false;
  }

  // Supprimer un grade
 supprimerGrade(gra: Grade) {
  if (confirm('Êtes-vous sûr de vouloir supprimer ce grade ?')) {
    this.employeservice.supprimerGrade(gra.idGraEmp!).subscribe({
      next: () => this.grades = this.grades.filter(g => g.idGraEmp !== gra.idGraEmp),
      error: err => alert("Erreur serveur : impossible de supprimer ce grade")
    });
  }
}

}
    

