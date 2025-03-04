import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  username: string | null;

  constructor(private authService: AuthService) {
    this.username = localStorage.getItem("username");
   }

  logout(){
    this.authService.logout();
  }

}
