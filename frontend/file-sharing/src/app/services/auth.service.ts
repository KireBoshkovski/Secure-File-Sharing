import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, tap } from 'rxjs';

const AUTH_API = 'https://localhost:8080/api/auth'
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  withCredentials: true,
  responseType: 'text' as 'json'
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private router: Router) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post<{ token: string }>(
      AUTH_API + '/login',
      { username: username, password: password }, { headers: { 'Content-Type': 'application/json' } })
      .pipe(
        tap((response) => {
          localStorage.setItem('username', username);
          localStorage.setItem('token', response.token);
        })
      );
  }

  register(username: string, email: string, password: string) {
    return this.http.post(AUTH_API + '/register', {
      username: username,
      email: email,
      password: password
    }, httpOptions)
  }

  logout() {
    localStorage.removeItem('token');
    return this.http.post(AUTH_API + '/logout', {})
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
