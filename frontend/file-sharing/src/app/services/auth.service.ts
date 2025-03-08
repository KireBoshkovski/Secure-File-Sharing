import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, tap } from 'rxjs';
import { HttpParams } from '@angular/common/http';

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
    return this.http.post<{ token: string, expiresIn: number }>(
      AUTH_API + '/login',
      { username: username, password: password }, { headers: { 'Content-Type': 'application/json' } })
      .pipe(
        tap((response) => {
          const expirationTime = Date.now() + response.expiresIn; // Calculate expiration time

          localStorage.setItem('username', username);
          localStorage.setItem('token', response.token);
          localStorage.setItem('expirationTime', expirationTime.toString());
        })
      );
  }

  verify(username: string, email: string, password: string) {
    return this.http.post(AUTH_API + '/verify', {
      username: username,
      email: email,
      password: password
    }, httpOptions)
  }

  register(token: string) {
    const params = new HttpParams().set('token', token);

    return this.http.post(AUTH_API + '/register', null, {
      ...httpOptions,
      params: params, 
    })
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
