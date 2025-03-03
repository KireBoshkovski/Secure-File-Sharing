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

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.formBuilder.group({
      username: [''],
      email: [''],
      password: ['']
    })
   }

  onSubmit() {
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
        console.log(err.error);
      }
    });
  }
}
