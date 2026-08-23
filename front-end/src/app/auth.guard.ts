import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { UserService } from './services/user/user.service';

// 1. Προστασία για σελίδες που απαιτούν Login (π.χ. /projects)
export class AuthGuard {
  static canActivate: CanActivateFn = () => {
    const userService = inject(UserService);
    const router = inject(Router);

    if (userService.getCurrentUser()) {
      return true;
    }

    router.navigate(['/login']);
    return false;
  };
}

// 2. Εμπόδιο για τη σελίδα Login αν ο χρήστης είναι ήδη συνδεδεμένος
export class UnauthGuard {
  static canActivate: CanActivateFn = () => {
    const userService = inject(UserService);
    const router = inject(Router);

    if (userService.getCurrentUser()) {
      router.navigate(['/projects']); // Αν είναι ήδη logged in, τον στέλνει στα projects!
      return false;
    }

    return true;
  };
}