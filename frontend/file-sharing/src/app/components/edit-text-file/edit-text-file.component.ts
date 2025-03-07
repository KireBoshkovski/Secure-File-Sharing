import { ActivatedRoute, Router } from '@angular/router';
import { FileService } from '../../services/file.service';
import { OnInit, Component } from '@angular/core';

@Component({
  selector: 'app-edit-text-file',
  standalone: false,
  templateUrl: './edit-text-file.component.html',
  styleUrl: './edit-text-file.component.css'
})
export class EditTextFileComponent implements OnInit {
  fileContent: any = '';
  fileId: number = -1;

  constructor(private route: ActivatedRoute, private fileService: FileService, private router: Router) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.fileId = params['id'];
      this.fetchFileContent(this.fileId);
    })
  }

  fetchFileContent(fileId: number) {
    this.fileService.viewFile(fileId, 'EDIT').subscribe(
      (blob: Blob) => {
        this.blobToString(blob).then((content) => {
          this.fileContent = content;
        })
      }
    )
  }

  private blobToString(blob: Blob): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        resolve(reader.result as string);
      };
      reader.onerror = () => {
        reject('Failed to read Blob as text');
      };
      reader.readAsText(blob); // Read the Blob as text
    });
  }

  saveChanges() {
    this.fileService.editFile(this.fileId, this.fileContent).subscribe(
      () => {
        alert("File updated successfully");
        window.close();
      },
      (error) => {
        console.error('Failed to update file:', error);
      }
    );
  }

  cancel() {
    window.close();
  }
}
