import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Grade } from '../model/Grade.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-update-grade',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './update-grade.html',
  styles: ``
})
export class UpdateGrade implements OnInit {
  @Input() grade!: Grade;
  @Output() gradeUpdated = new EventEmitter<Grade>();
  @Input() ajout!: boolean;

  ngOnInit(): void {
    console.log(this.grade);
  }

  saveGrade() {
    this.gradeUpdated.emit(this.grade);
  }
}
