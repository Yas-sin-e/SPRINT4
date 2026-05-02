import { Component, OnInit } from '@angular/core';
import { Employees } from '../model/employees.model';
import { EmpServices } from '../services/emp-services';
import { Auth } from '../services/auth';
import { RouterLink } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { HasRolesDirective } from 'keycloak-angular';

@Component({
  selector: 'app-employe',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe, HasRolesDirective],
  templateUrl: './employe.html',
})
export class Employe implements OnInit {
  apiURL: string = 'http://localhost:8083/Employees/api';
  employes!: Employees[];

  constructor(
    private employeservice: EmpServices,
    public authService: Auth
  ) { }

  ngOnInit(): void {
    this.rechargerEMP();
  }

  // recharger() {
  //   this.employeservice.listerEmp().subscribe({
  //     next: (emps) => {
  //       this.employes = emps;
  //       this.employes.forEach(emp => this.loadPrimaryImage(emp));
  //     },
  //     error: (err) => console.error('Erreur chargement employés:', err)
  //   });
  // }
  rechargerEMP() {
    this.employeservice.listerEmp().subscribe(emps => {
      this.employes = emps;
    });
  }

  // private loadPrimaryImage(emp: Employees) {
  //   if (!emp.idEmploye) return;

  //   this.employeservice.getImagesByEmp(emp.idEmploye).subscribe({
  //     next: (imgs: Image[]) => {
  //       if (imgs && imgs.length > 0) {
  //         emp.images = imgs;
  //         emp.imageStr = 'data:' + imgs[0].type + ';base64,' + imgs[0].image;
  //       }
  //     },
  //     error: () => {
  //       if (emp.image?.idImage) {
  //         this.employeservice.loadImage(emp.image.idImage).subscribe((img: Image) => {
  //           emp.imageStr = 'data:' + img.type + ';base64,' + img.image;
  //         });
  //       }
  //     }
  //   });
  // }

  supprimerEmploye(emp: Employees) {
    if (confirm('Etes-vous sûr ?') && emp.idEmploye) {
      // Supprimer d'abord l'image du système de fichiers si elle existe
      if (emp.imagePath) {
        this.employeservice.supprimerImageFS(emp.idEmploye).subscribe({
          next: () => console.log('Image supprimée du dossier'),
          error: err => console.error('Erreur suppression image:', err)
        });
      }

      // Supprimer l'employé
      this.employeservice.supprimerEmp(emp.idEmploye).subscribe({
        next: () => {
          console.log('Employé supprimé');
          this.rechargerEMP();
        },
        error: err => console.error('Erreur suppression employé:', err)
      });
    }
  }
}
