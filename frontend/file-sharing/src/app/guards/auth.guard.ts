
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(): boolean {
    const token = localStorage.getItem('token');
    const expirationTime = localStorage.getItem('expirationTime');

    if (token && expirationTime) {
      const isTokenExpired = Date.now() >= +expirationTime;
      if (!isTokenExpired) {
        return true;
      }
    }
    this.router.navigate(['/login']);
    return false;
  }
}