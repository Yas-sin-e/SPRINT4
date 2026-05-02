import { Injectable } from '@angular/core';
import { Employees } from '../model/employees.model';
import { Grade } from '../model/Grade.model';
import { Image } from '../model/image.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class EmpServices {

  apiURL: string = 'http://localhost:8083/Employees/api';
  imageURL: string = 'http://localhost:8083/Employees/api/image';

  constructor(private http: HttpClient) { }

  // ─── EMPLOYÉS ────────────────────────────────────────────
  listerEmp(): Observable<Employees[]> {
    return this.http.get<Employees[]>(this.apiURL + '/all');
  }

  consulterEmployee(id: number): Observable<Employees> {
    return this.http.get<Employees>(this.apiURL + '/getbyid/' + id);
  }

  ajouterEmp(emp: Employees): Observable<Employees> {
    return this.http.post<Employees>(this.apiURL + '/addemp', emp);
  }

  updateEmp(emp: Employees): Observable<Employees> {
    return this.http.put<Employees>(this.apiURL + '/updateemp', emp);
  }

  supprimerEmp(id: number): Observable<void> {
    return this.http.delete<void>(this.apiURL + '/delemp/' + id);
  }

  rechercherParGrade(idGra: number): Observable<Employees[]> {
    return this.http.get<Employees[]>(this.apiURL + '/EmployeeGrade/' + idGra);
  }

  // ─── GRADES ──────────────────────────────────────────────
  listegrades(): Observable<Grade[]> {
    return this.http.get<Grade[]>(`${environment.apiGradeURL}`);
  }

  ajouterGrade(gra: Grade): Observable<Grade> {
    return this.http.post<Grade>(`${environment.apiGradeURL}`, gra, httpOptions);
  }

  updateGrade(gra: Grade): Observable<Grade> {
    return this.http.put<Grade>(`${environment.apiGradeURL}/${gra.idGraEmp}`, gra, httpOptions);
  }

  supprimerGrade(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiGradeURL}/${id}`);
  }

  getGradeById(id: number): Observable<Grade> {
    return this.http.get<Grade>(`${environment.apiGradeURL}/${id}`);
  }

  // ─── IMAGES (BASE DE DONNÉES - ACTIF) ────────────────────

  // Charger les détails d'une image depuis la BD (base64)
  loadImage(id: number): Observable<Image> {
    return this.http.get<Image>(`${this.imageURL}/get/info/${id}`);
  }

  // Supprimer une image de la BD
  supprimerImage(id: number): Observable<void> {
    return this.http.delete<void>(`${this.imageURL}/delete/${id}`);
  }

  // Upload image dans la BD (plusieurs images par employé)
  uploadImageProd(file: File, filename: string, idEmp: number): Observable<Image> {
    const formData = new FormData();
    formData.append('image', file, filename);
    return this.http.post<Image>(`${this.imageURL}/uploadImageEmp/${idEmp}`, formData);
  }

  // Récupérer toutes les images d'un employé depuis la BD
  getImagesByEmp(idEmp: number): Observable<Image[]> {
    return this.http.get<Image[]>(`${this.imageURL}/getImagesByEmp/${idEmp}`);
  }

  // ─── STOCKAGE SYSTÈME DE FICHIERS (COMMENTÉ) ────────────

  // Upload image dans le dossier home/images (écrase l'ancienne image)
  uploadImageFS(file: File, filename: string, idEmp: number): Observable<any> {
    const formData = new FormData();
    formData.append('image', file, filename);
    return this.http.post(`${this.imageURL}/uploadFS/${idEmp}`, formData, { responseType: 'text' });
  }

  // Charger image depuis le dossier home/images
  getImageFS(id: number): Observable<Blob> {
    return this.http.get(`${this.imageURL}/loadfromFS/${id}`, { responseType: 'blob' });
  }

  // Supprimer image du système de fichiers (dossier images)
  supprimerImageFS(idEmp: number): Observable<void> {
    return this.http.delete<void>(`${this.imageURL}/deleteFS/${idEmp}`);
  }
}
