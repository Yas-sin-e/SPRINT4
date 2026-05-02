import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { EmpServices } from '../services/emp-services';
import { Router } from '@angular/router';
import { Grade } from '../model/Grade.model';
import { Employees } from '../model/employees.model';

@Component({
  selector: 'app-add-employe',
  standalone: true,
  templateUrl: './add-employe.html',
  styleUrls: ['./add-employe.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class AddEmploye implements OnInit {

  empForm!: FormGroup;
  grades: Grade[] = [];
  currentPreview: string | null = null;
  currentFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private employeService: EmpServices,
    private router: Router,
  ) { }

  ngOnInit() {
    this.employeService.listegrades().subscribe({
      next: (g) => { this.grades = g; },
      error: (err) => console.error('Erreur chargement Grades:', err)
    });

    this.empForm = this.fb.group({
      idEmploye: [''],
      nomEmploye: ['', [Validators.required, Validators.minLength(3)]],
      prenomEmploye: ['', Validators.required],
      posteEmploye: ['', Validators.required],
      dateEmbauche: ['', Validators.required],
      salaire: ['', [Validators.required, Validators.min(1)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', [Validators.required, Validators.pattern(/^[0-9]{8}$/)]],
      adresse: ['', Validators.required],
      idGra: ['', Validators.required],
    });
  }

  private convertToDate(dateString: string): Date | undefined {
    if (!dateString) return undefined;
    const parts = dateString.split('-');
    if (parts.length !== 3) return undefined;
    return new Date(+parts[0], +parts[1] - 1, +parts[2]);
  }

  onImageUpload(event: any) {
    const file: File = event.target.files?.[0];
    if (!file) return;
    this.currentFile = file;
    const reader = new FileReader();
    reader.onload = () => { this.currentPreview = reader.result as string; };
    reader.readAsDataURL(file);
    event.target.value = '';
  }

  removeImage() {
    this.currentFile = null;
    this.currentPreview = null;
  }

  triggerFileInput() {
    const fileInput = document.getElementById('imagesInput') as HTMLInputElement;
    if (fileInput) fileInput.click();
  }

  addEmploye() {
    if (this.empForm.invalid) {
      this.empForm.markAllAsTouched();
      return;
    }

    const form = this.empForm.value;
    const selectedGrade = this.grades.find(g => g.idGraEmp == form.idGra);
    if (!selectedGrade) return;

    const newEmployee: Employees = {
      nomEmploye: form.nomEmploye,
      prenomEmploye: form.prenomEmploye,
      posteEmploye: form.posteEmploye,
      dateEmbauche: this.convertToDate(form.dateEmbauche),
      salaire: form.salaire,
      email: form.email,
      telephone: form.telephone,
      adresse: form.adresse,
      grade: selectedGrade,
      showDetails: false
    };

this.employeService.ajouterEmp(newEmployee).subscribe({
      next: (savedEmployee) => {
        const idEmp = savedEmployee.idEmploye;

        // Upload d'une seule image si elle existe (écrase l'ancienne)
        if (this.currentFile && idEmp) {
          this.employeService.uploadImageFS(this.currentFile, this.currentFile.name, idEmp).subscribe({
            next: () => {
              // Recharger la page pour voir le nouvel employé
              window.location.href = '/employe';
            },
            error: err => console.error('Erreur upload image:', err)
          });
        } else {
          window.location.href = '/employe';
        }
      },
      error: err => console.error('Erreur ajout employé:', err)
    });
  }
}
