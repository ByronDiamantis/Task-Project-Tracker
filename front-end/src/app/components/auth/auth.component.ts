import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { User } from '../../models/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth',
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class AuthComponent {

  isLoginMode = true;

  userForm: User & { password?: string } = {
    username: '',
    email: '',
    password: ''
  };

  errorMessage: string = "";
  isLoading: boolean = false;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = '';
    this.resetForm();
  }

  onSubmit(): void {
    // Στο Login απαιτούμε email & password
    if (this.isLoginMode && (!this.userForm.email || !this.userForm.password)) {      
      this.errorMessage = 'Παρακαλώ συμπληρώστε όλα τα υποχρεωτικά πεδία.';
      return;
    }

    // Στο Register απαιτούμε username, email & password
    if (!this.isLoginMode && (!this.userForm.username || !this.userForm.email || !this.userForm.password)) {
      this.errorMessage = 'Το Email είναι υποχρεωτικό για την εγγραφή.';
      return;
    }

    this.errorMessage = '';
    this.isLoading = true;

    if (this.isLoginMode) {
      //Login
      this.userService.loginUser({
        email: this.userForm.email,
        password: this.userForm.password
      }).subscribe({ 
        next: (userResponse: User) => { 
          this.isLoading = false; 
          this.userService.setUserInStorage(userResponse);
          this.router.navigate(['/projects'], { replaceUrl: true });      
        },
        error: (err) => {
          this.isLoading = false;
          console.error('Login error', err);
          this.errorMessage = 'Λάθος όνομα χρήστη ή κωδικός πρόσβασης';
        } 
      });
    } else {
      //register
      this.userService.register(this.userForm as User).subscribe({
        next: () => { 
          this.isLoading = false; 
          this.isLoginMode = true;
        },
        error: (err) => { 
          this.isLoading = false;
          console.error('Registration error:', err);
          this.errorMessage = 'Αποτυχία εγγραφής. Το userrname μπορεί να χρησιμοποιείται ήδη.';
        } 
      });
    }
  }

  private resetForm(): void {
    this.userForm = {
      username: '',
      email: '',
      password: ''
    };
  }

}
