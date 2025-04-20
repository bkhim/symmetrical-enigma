import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.authService.isLoggedIn()) {
      console.log('This is in the auth guard user is logged in');
      return true;  // Allow access to dashboard if logged in
    } else {
      console.log('This is in the auth guard not logged in');

      this.router.navigate(['/login']);  // Redirect to login if not logged in
      return false;
    }
  }
}
