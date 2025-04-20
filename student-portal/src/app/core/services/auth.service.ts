import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

interface User {
  id: string;
  firstName: string;
  lastName: string;
  studentId: string;
}

interface AuthResponse {
  token: string;
  user: User;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly authState = new BehaviorSubject<boolean>(false);
  private readonly currentUser = new BehaviorSubject<User | null>(null);

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.initializeAuthState();
  }

  // Public observables
  public readonly authState$ = this.authState.asObservable();
  public readonly currentUser$ = this.currentUser.asObservable();

  login(credentials: { username: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap({
        next: (response) => this.handleLoginSuccess(response),
        error: (error) => this.handleLoginError(error)
      })
    );
  }

  logout(): void {
    this.clearAuthData();
    this.authState.next(false);
    this.currentUser.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.safeLocalStorageGet('jwt_token');
  }

  getCurrentUser(): User | null {
    return this.currentUser.value;
  }

  isLoggedIn(): boolean {
    return this.authState.value;
  }

  private initializeAuthState(): void {
    if (this.isBrowser) {
      const token = this.getToken();
      const user = this.safeLocalStorageGet('user_data');

      if (token && user) {
        try {
          this.authState.next(true);
          this.currentUser.next(JSON.parse(user));
        } catch (e) {
          this.clearAuthData();
        }
      }
    }
  }

  private handleLoginSuccess(response: AuthResponse): void {
    this.safeLocalStorageSet('jwt_token', response.token);
    this.safeLocalStorageSet('user_data', JSON.stringify(response.user));
    this.authState.next(true);
    this.currentUser.next(response.user);
    this.router.navigate(['/dashboard']);
  }

  private handleLoginError(error: any): void {
    this.clearAuthData();
    console.error('Login failed:', error);
  }

  private clearAuthData(): void {
    this.safeLocalStorageRemove('jwt_token');
    this.safeLocalStorageRemove('user_data');
  }

  // Safe localStorage access methods
  private get isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  private safeLocalStorageGet(key: string): string | null {
    return this.isBrowser ? localStorage.getItem(key) : null;
  }

  private safeLocalStorageSet(key: string, value: string): void {
    if (this.isBrowser) {
      localStorage.setItem(key, value);
    }
  }

  private safeLocalStorageRemove(key: string): void {
    if (this.isBrowser) {
      localStorage.removeItem(key);
    }
  }
}
