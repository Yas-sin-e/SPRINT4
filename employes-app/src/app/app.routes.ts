import { Routes } from '@angular/router';
import { Employe } from './employe/employe';
import { AddEmploye } from './add-employe/add-employe';
import { UpdateEmploye } from './update-employe/update-employe';
import { RechercheParGrade } from './recherche-par-grade/recherche-par-grade';
import { RechercheParNom } from './recherche-par-nom/recherche-par-nom';
import { ListeGrade } from './liste-grade/liste-grade';
import { UserProfile } from './user-profile/user-profile';
import { canActivateAuthRole } from './guards/auth-role.guard';
import { Forbidden } from './forbidden/forbidden';

export const routes: Routes = [
  { path: 'employe', component: Employe},
  { path: 'rechercheParGrade', component: RechercheParGrade },
  { path: 'rechercheParNom', component: RechercheParNom },
  { path: 'add_employe', component: AddEmploye,canActivate: [canActivateAuthRole],
    data: { role: 'ADMIN' } },
  { path: 'updateEmploye/:id', component: UpdateEmploye,canActivate: [canActivateAuthRole],
    data: { role: 'ADMIN' } },
  { path: 'listeGrade', component: ListeGrade },
  // { path: '', redirectTo: 'employe', pathMatch: 'full' },
    { path: 'profile', component: UserProfile },
     { path: 'forbidden', component: Forbidden }
];
