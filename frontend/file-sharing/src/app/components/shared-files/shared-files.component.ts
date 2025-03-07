import { Component, OnInit } from '@angular/core';
import { FileService } from '../../services/file.service';
import { Router, NavigationEnd } from '@angular/router';


@Component({
  selector: 'app-shared-files',
  standalone: false,
  templateUrl: './shared-files.component.html',
  styleUrl: './shared-files.component.css'
})
export class SharedFilesComponent implements OnInit {
  sharedFiles: any[] = [];

  constructor(private fileService: FileService, private router: Router) { }

  ngOnInit(): void {
    this.loadCreatedFiles();
  }

  loadCreatedFiles() {
    this.fileService.getAccessibleFiles().subscribe({
      next: (data) => this.sharedFiles = data,
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
      this.router.createUrlTree(['/edit-text-file'], {
        queryParams: { id: fileId },
      })
    );
    window.open(url, '_blank');
  }
}
