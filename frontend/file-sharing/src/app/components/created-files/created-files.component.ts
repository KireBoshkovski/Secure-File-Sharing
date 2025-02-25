import { Component, OnInit } from '@angular/core';
import { FileService } from '../../services/file.service';

@Component({
  selector: 'app-created-files',
  standalone: false,
  templateUrl: './created-files.component.html',
  styleUrl: './created-files.component.css'
})
export class CreatedFilesComponent implements OnInit {
  createdFiles: any[] = [];

  constructor(private fileService: FileService) { }

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

  share(fileId: number, username: string) {

    const accessType = "READ";
    console.log(`Sharing file with ID: ${fileId} with ${username} with access type ${accessType}`);

    this.fileService.shareFile(fileId, username, accessType).subscribe({
      next: () => this.loadCreatedFiles()
    })
  }

}
