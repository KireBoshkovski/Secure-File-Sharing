import { Component } from '@angular/core';
import { FileService } from '../../services/file.service';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-shared-files',
  standalone: false,
  templateUrl: './shared-files.component.html',
  styleUrls: ['./shared-files.component.css']
})
export class SharedFilesComponent {
  sharedFiles: any[] = [];

  constructor(private fileService: FileService, private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.loadCreatedFiles();
      }
    });
  }

  loadCreatedFiles() {
    this.fileService.getAccessibleFiles().subscribe({
      next: (data) => this.sharedFiles = data,
    });
  }

  open(fileId: number) {
    this.fileService.viewFile(fileId).subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);

      window.open(url, '_blank');

      window.URL.revokeObjectURL(url);
    });
  }

  download(fileId: number, filename: string) {
    this.fileService.downloadFile(fileId).subscribe(
      (response: Blob) => {
        const url = window.URL.createObjectURL(response);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      (error) => {
        console.error('Error downloading file:', error);
      }
    );
  }

  edit(fileId: number) {
    this.router.navigate([`/edit/${fileId}`]);
  }
}
