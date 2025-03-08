import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-user',
  standalone: false,
  templateUrl: './verify-user.component.html',
  styleUrl: './verify-user.component.css'
})
export class VerifyUserComponent {
  code: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit() {
    this.authService.register(this.code).subscribe({
      next: () => {
        alert('Email successfully verified...');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.log(err.error);
      }
    });
  }
}
