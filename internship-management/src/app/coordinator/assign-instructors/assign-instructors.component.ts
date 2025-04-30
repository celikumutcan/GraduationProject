import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-assign-instructors',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assign-instructors.component.html',
  styleUrl: './assign-instructors.component.css'
})
export class AssignInstructorsComponent {
  instructors = ['Enver Ever', 'Yeliz Yeşilada', 'Şükrü Eraslan', 'Meryem Erbilek', 'Muhammed Toaha Raza Khan', 'Mariam Hmila', 'Muhammed Sohail', 'Muhammed Akif Ağca', 'Ahmet Coşar',];

  // Örnek trainee formlar (gerçekte backend'den gelecek)
  traineeForms = [
    { id: 1, name: 'Ali Fırat Özdemir', status: 'Approved', assignedInstructor: '' },
    { id: 2, name: 'Ayşe Yılmaz', status: 'Pending', assignedInstructor: '' },
    { id: 3, name: 'Umutcan Çelik', status: 'Approved', assignedInstructor: '' }
  ];

  assign(form: any) {
    console.log(`Assigned ${form.assignedInstructor} to ${form.name}`);
    // Gerçek kullanımda burada HTTP çağrısı yapılacak
  }
}
