import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-forms',
  standalone: true,
  imports: [CommonModule], // CommonModule eklendi
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css'],
})
export class FormsComponent {
  forms: Array<{ id: number; subject: string; file: string; dateAdded: string }> = []; // Boş Form Listesi

  uploadError: string | null = null; // Hata mesajları için

  addForm(event: any) {
    const file: File = event.target.files[0]; // Seçilen dosyayı al
    if (file && file.type === 'application/pdf') {
      const newForm = {
        id: this.forms.length + 1,
        subject: file.name,
        file: file.name, // Dosya adını göster
        dateAdded: new Date().toLocaleString(), // Tarihi okunabilir formatta ayarla
      };
      this.forms.push(newForm); // Yeni formu listeye ekle
      this.uploadError = null; // Hata mesajını sıfırla
    } else {
      this.uploadError = 'Only PDF files are allowed.'; // Hata mesajı
    }
  }

  deleteForm(id: number) {
    this.forms = this.forms.filter((form) => form.id !== id); // Formu listeden kaldır
  }
}
