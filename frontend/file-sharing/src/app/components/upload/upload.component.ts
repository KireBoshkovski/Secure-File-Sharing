import { Component } from '@angular/core';
import { FileService } from '../../services/file.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-upload',
  standalone: false,
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css'
})
export class UploadComponent {
  selectedFile: any;

  constructor(private fileService: FileService, private router: Router) { }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  onUpload(): void {
    this.fileService.uploadFile(this.selectedFile).subscribe({
      next: (response) => {
        console.log(response);
      }
    });

    this.clearFileInput();
  }

  clearFileInput(): void {
    this.selectedFile = null;

    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = ''; 
    }
  }
}
