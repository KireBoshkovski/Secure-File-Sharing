import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const FILE_API = 'http://localhost:8080/api/files';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  constructor(private http: HttpClient) { }

  // Helper method to get HTTP headers with JWT token
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  // Upload a file
  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${FILE_API}/upload`, formData, {
      headers: this.getAuthHeaders(),
    });
  }

  // Download a file
  downloadFile(fileId: number): Observable<Blob> {
    return this.http.get(`${FILE_API}/download/${fileId}`, {
      headers: this.getAuthHeaders(),
      responseType: 'blob',
    });
  }

  // Delete a file
  deleteFile(fileId: number): Observable<any> {
    return this.http.post(
      `${FILE_API}/delete/${fileId}`,
      {},
      { headers: this.getAuthHeaders() }
    );
  }

  // Get files accessible by the user
  getAccessibleFiles(): Observable<any> {
    return this.http.get(`${FILE_API}/access`, {
      headers: this.getAuthHeaders(),
    });
  }

  // Get files created by the user
  getCreatedFiles(): Observable<any> {
    return this.http.get(`${FILE_API}/created`, {
      headers: this.getAuthHeaders(),
    });
  }

  // Share a file with another user
  shareFile(fileId: number, username: string, accessType: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('accessType', accessType);

    return this.http.post(`${FILE_API}/share/${fileId}`, null , { params, headers: this.getAuthHeaders() });
  }
}
