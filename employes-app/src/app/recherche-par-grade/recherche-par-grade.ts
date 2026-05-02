import { Component } from '@angular/core';
import { Employees } from '../model/employees.model';
import { Grade } from '../model/Grade.model';
import { EmpServices } from '../services/emp-services';
import { DatePipe, CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Auth } from '../services/auth';

@Component({
  selector: 'app-recherche-par-grade',
  imports: [FormsModule, DatePipe, RouterLink, CommonModule],
  templateUrl: './recherche-par-grade.html',
})
export class RechercheParGrade {

  employes: Employees[] = [];
  grades!: Grade[];
  IdGrade!: number;
  apiURL: string = 'http://localhost:8083/Employees/api';

  constructor(private employeservice: EmpServices, public authService: Auth) { }

  ngOnInit() {
    this.employeservice.listegrades().subscribe(g => this.grades = g);
  }

  onChange() {
    this.employeservice.rechercherParGrade(this.IdGrade).subscribe(emp => {
      this.employes = emp;
    });
  }

  supprimerEmploye(emp: Employees) {
    if (confirm('Etes-vous sûr ?') && emp.idEmploye) {
      // Supprimer l'image du dossier si elle existe
      if (emp.imagePath) {
        this.employeservice.supprimerImageFS(emp.idEmploye).subscribe({
          next: () => console.log('Image supprimée'),
          error: err => console.error('Erreur suppression image:', err)
        });
      }

      this.employeservice.supprimerEmp(emp.idEmploye).subscribe({
        next: () => this.onChange(),
        error: err => console.error('Erreur suppression employé:', err)
      });
    }
  }
}
