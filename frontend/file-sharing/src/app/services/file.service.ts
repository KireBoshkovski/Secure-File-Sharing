import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';

const FILE_API = 'https://localhost:8080/api/files';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${FILE_API}/upload`, formData, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  downloadFile(fileId: number): Observable<Blob> {
    const params = new HttpParams().set('accessRequest', 'DOWNLOAD');

    return this.http.get(`${FILE_API}/download/${fileId}`, {
      headers: this.getAuthHeaders(),
      params: params,
      responseType: 'blob',
    });
  }

  deleteFile(fileId: number): Observable<any> {
    return this.http.post(
      `${FILE_API}/delete/${fileId}`,
      {},
      { headers: this.getAuthHeaders() }
    );
  }

  getAccessibleFiles(): Observable<any> {
    return this.http.get(`${FILE_API}/access`, {
      headers: this.getAuthHeaders(),
    });
  }

  getCreatedFiles(): Observable<any> {
    return this.http.get(`${FILE_API}/created`, {
      headers: this.getAuthHeaders(),
    });
  }

  shareFile(fileId: number, username: string, accessType: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('accessType', accessType);

    return this.http.post(`${FILE_API}/share/${fileId}`, null, { params, headers: this.getAuthHeaders(), responseType: 'text' });
  }

  viewFile(fileId: number) {
    const params = new HttpParams().set('accessRequest', 'VIEW');

    return this.http.get(`${FILE_API}/download/${fileId}`, {
      headers: this.getAuthHeaders(),
      params: params,
      responseType: 'blob',
    });
  }
}
