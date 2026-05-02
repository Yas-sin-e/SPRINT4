import { Grade } from "./Grade.model";
import { Image } from "./image.model";

export class Employees {
  idEmploye?: number;
  nomEmploye?: string;
  prenomEmploye?: string;
  posteEmploye?: string;
  dateEmbauche?: Date;
  salaire?: number;
  email?: string;
  telephone?: string;
  adresse?: string;
  grade!: Grade;
  showDetails!: boolean;
  image?: Image;//ca sera pour stocker l'image de l'employé pour l'afficher dans les détails de l'employé
  imageStr?: string;//ca sera pour stocker l'image en base64 pour l'afficher dans la liste des employés et dans les détails de l'employé str c'est a dire string de l'image
  images?: Image[];
  imagePath?: string;
}
