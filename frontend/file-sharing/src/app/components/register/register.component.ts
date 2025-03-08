import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  showPassword: boolean = false;
  usernameError: string = '';
  emailError: string = '';

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.formBuilder.group({
      username: [''],
      email: [''],
      password: ['']
    });

    this.registerForm.valueChanges.subscribe(() => {
      this.usernameError = '';
      this.emailError = '';
    });
  }

  onSubmit() {
    this.usernameError = '';
    this.emailError = '';

    this.authService.register(
      this.registerForm.get('username')?.value,
      this.registerForm.get('email')?.value,
      this.registerForm.get('password')?.value
    ).subscribe({
      next: () => {
        alert('Registration successful! Redirecting to login...');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        if (err.status === 400) {
          const errorMessage = err.error;
          if (typeof errorMessage === 'string') {
            if (errorMessage.includes('Username')) {
              this.usernameError = errorMessage;
            } else if (errorMessage.includes('Email')) {
              this.emailError = errorMessage;
            }
          }
        } else {
          console.log(err.error);
        }
      }
    });
  }

  toggleShowPassword() {
    this.showPassword = !this.showPassword;
  }
}
