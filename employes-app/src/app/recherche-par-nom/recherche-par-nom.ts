import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EmpServices } from '../services/emp-services';
import { Employees } from '../model/employees.model';
import { Auth } from '../services/auth';

@Component({
  selector: 'app-recherche-par-nom',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DatePipe],
  templateUrl: './recherche-par-nom.html',
})
export class RechercheParNom implements OnInit {

  employes: Employees[] = [];
  searchTerm: string = '';
  apiURL: string = 'http://localhost:8083/Employees/api';

  constructor(private employeservice: EmpServices, public authService: Auth) { }

  ngOnInit(): void {
    this.employeservice.listerEmp().subscribe(data => {
      this.employes = data;
    });
  }

  get filteredEmployes(): Employees[] {
    if (!this.searchTerm) return this.employes;
    const term = this.searchTerm.toLowerCase().trim();
    return this.employes.filter(emp =>
      (emp.nomEmploye?.toLowerCase().includes(term)) ||
      (emp.prenomEmploye?.toLowerCase().includes(term))
    );
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
        next: () => {
          this.employes = this.employes.filter(e => e.idEmploye !== emp.idEmploye);
        },
        error: err => console.error('Erreur suppression employé:', err)
      });
    }
  }
}
