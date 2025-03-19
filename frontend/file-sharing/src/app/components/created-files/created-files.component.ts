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

  showSharePopup: boolean = false;
  selectedFileId: number = 1;

  constructor(private fileService: FileService, private router: Router) { }

  ngOnInit(): void {
    this.loadCreatedFiles();
  }

  loadCreatedFiles() {
    this.fileService.getCreatedFiles().subscribe({
      next: (data) => this.createdFiles = data,
    });
  }

  open(fileId: number) {
    this.fileService.viewFile(fileId, 'VIEW').subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);

      window.open(url, '_blank');

      window.URL.revokeObjectURL(url);
    });
  }

  download(fileId: number, fileName: string) {
    this.fileService.downloadFile(fileId).subscribe(blob => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      a.download = fileName;
      a.click();
      URL.revokeObjectURL(objectUrl);
    });
  }

  openEditor(fileId: number) {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/edit'], {
        queryParams: { id: fileId },
      })
    );
    window.open(url, '_blank');
  }

  openSharePopup(fileId: number): void {
    this.selectedFileId = fileId;
    this.showSharePopup = true;
  }

  onShare(event: { username: string, permission: any }) {
    const fileId = this.selectedFileId;
    const username = event.username;
    const accessType = event.permission;

    console.log('Sharing with:', event.username, 'Permissions:', event.permission);
    this.fileService.shareFile(fileId, username, accessType).subscribe({
      next: (response) => {
        console.log('File shared successfully:', response);
        this.showSharePopup = false;
      },
      error: (error) => {
        console.error('Error sharing file:', error);
      },
    })

    this.showSharePopup = false;
  }

  delete(fileId: number) {
    this.fileService.deleteFile(fileId).subscribe({
      next: () => this.loadCreatedFiles(),
      error: (err) => console.error('Delete failed', err),
    });
  }
}
