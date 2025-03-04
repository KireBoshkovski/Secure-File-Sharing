import { Component, OnInit } from '@angular/core';
import { FileService } from '../../services/file.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-created-files',
  standalone: false,
  templateUrl: './created-files.component.html',
  styleUrl: './created-files.component.css'
})
export class CreatedFilesComponent implements OnInit {
  createdFiles: any[] = [];

  constructor(private fileService: FileService, private router: Router) { }

  ngOnInit(): void {
    this.loadCreatedFiles();
  }

  loadCreatedFiles() {
    this.fileService.getCreatedFiles().subscribe({
      next: (data) => this.createdFiles = data,
    });
  }

  download(fileId: number) {
    this.fileService.downloadFile(fileId).subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      a.download = 'file';
      a.click();
      URL.revokeObjectURL(objectUrl);
    });
  }

  delete(fileId: number) {
    this.fileService.deleteFile(fileId).subscribe({
      next: () => this.loadCreatedFiles(),
      error: (err) => console.error('Delete failed', err),
    });
  }

  share(fileId: number, usernameInput: HTMLInputElement) {
    const username = usernameInput.value;
    const accessType = "VIEW";

    this.fileService.shareFile(fileId, username, accessType).subscribe({
      next: (response) => {
        console.log(response);
      }
    });

    usernameInput.value = '';
    this.router.navigate(['/created-files']);
  }
}
