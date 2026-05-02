
import { Grade } from './Grade.model';
export class GradeWrapper {
  _embedded!: { grade: Grade[] };
}
