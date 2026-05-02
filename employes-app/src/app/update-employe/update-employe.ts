import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EmpServices } from '../services/emp-services';
import { Grade } from '../model/Grade.model';
import { Employees } from '../model/employees.model';

@Component({
  selector: 'app-update-employe',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-employe.html',
  styleUrls: ['./update-employe.css']
})
export class UpdateEmploye implements OnInit {

  editForm!: FormGroup;
  grades: Grade[] = [];
  currentEmploye!: Employees;
  currentPreview: string | null = null;
  currentFile: File | null = null;
  existingImageUrl: string | null = null;
  apiURL: string = 'http://localhost:8083/Employees/api';

  constructor(
    private fb: FormBuilder,
    private employeService: EmpServices,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    const id = +this.route.snapshot.params['id'];

    this.employeService.listegrades().subscribe(gr => {
      this.grades = gr;
    });

    this.employeService.consulterEmployee(id).subscribe(emp => {
      this.currentEmploye = emp;

      // Charger l'image existante depuis le dossier (file system)
      if (emp.idEmploye && emp.imagePath) {
        this.existingImageUrl = `${this.apiURL}/image/loadfromFS/${emp.idEmploye}`;
      }

      this.initForm();
    });
  }

  private initForm() {
    this.editForm = this.fb.group({
      idEmploye: [''],
      nomEmploye: ['', [Validators.required, Validators.minLength(3)]],
      prenomEmploye: ['', Validators.required],
      posteEmploye: ['', Validators.required],
      dateEmbauche: ['', Validators.required],
      salaire: ['', [Validators.required, Validators.min(1)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', [Validators.required, Validators.pattern(/^[0-9]{8}$/)]],
      adresse: ['', Validators.required],
      idGra: ['', Validators.required]
    });

    setTimeout(() => {
      if (this.currentEmploye) {
        this.editForm.patchValue({
          idEmploye: this.currentEmploye.idEmploye,
          nomEmploye: this.currentEmploye.nomEmploye,
          prenomEmploye: this.currentEmploye.prenomEmploye,
          posteEmploye: this.currentEmploye.posteEmploye,
          dateEmbauche: this.formatDateString(this.currentEmploye.dateEmbauche),
          salaire: this.currentEmploye.salaire,
          email: this.currentEmploye.email,
          telephone: this.currentEmploye.telephone,
          adresse: this.currentEmploye.adresse,
          idGra: this.currentEmploye.grade?.idGraEmp
        });
      }
    }, 0);
  }

  private formatDateString(date: any): string {
    if (!date) return '';
    const d = new Date(date);
    if (isNaN(d.getTime())) return '';
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
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

  updateEmploye() {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const form = this.editForm.value;
    const selectedGrade = this.grades.find(g => g.idGraEmp == form.idGra);
    if (!selectedGrade) return;

    const updatedEmploye: Employees = {
      idEmploye: form.idEmploye,
      nomEmploye: form.nomEmploye,
      prenomEmploye: form.prenomEmploye,
      posteEmploye: form.posteEmploye,
      dateEmbauche: this.convertToDate(form.dateEmbauche),
      salaire: form.salaire,
      email: form.email,
      telephone: form.telephone,
      adresse: form.adresse,
      grade: selectedGrade,
      showDetails: this.currentEmploye.showDetails,
      imagePath: this.currentEmploye.imagePath
    };

    // Mise à jour d'abord, puis upload de l'image si elle existe
    this.employeService.updateEmp(updatedEmploye).subscribe({
      next: (savedEmployee) => {
        const idEmp = savedEmployee.idEmploye;

        // Upload d'une seule image si elle existe (écrase l'ancienne)
        if (this.currentFile && idEmp) {
          this.employeService.uploadImageFS(this.currentFile, this.currentFile.name, idEmp).subscribe({
            next: () => {
              // Recharger la page pour voir les modifications
              window.location.href = '/employe';
            },
            error: err => console.error('Erreur upload image:', err)
          });
        } else {
          window.location.href = '/employe';
        }
      },
      error: err => console.error('Erreur mise à jour employé:', err)
    });
  }
}
