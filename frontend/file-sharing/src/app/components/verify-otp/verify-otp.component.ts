import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-otp',
  standalone: false,
  templateUrl: './verify-otp.component.html',
  styleUrl: './verify-otp.component.css'
})
export class VerifyOtpComponent {
  otp: string = '';
  error: string = '';
  username: string;

  constructor(private router: Router, private authService: AuthService) {
    this.username = localStorage.getItem('username') || '';
   }

  onSubmit() {
    this.error = '';

    this.authService.sendotp(this.otp, this.username).subscribe({
      next: (response) => {
        console.log(response);
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.error = err.error;
      }
    });
  }
}
