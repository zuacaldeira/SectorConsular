import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

interface AuthResponse {
  token: string;
  role: string;
  expiresIn: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'sgcd_pm_token';
  private roleKey = 'sgcd_pm_role';
  private isAuthenticated$ = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private api: ApiService, private router: Router) {}

  login(username: string, password: string): Observable<AuthResponse> {
    return this.api.post<AuthResponse>('/auth/login', { username, password }).pipe(
      tap(res => {
        localStorage.setItem(this.tokenKey, res.token);
        localStorage.setItem(this.roleKey, res.role);
        this.isAuthenticated$.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.roleKey);
    this.isAuthenticated$.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getRole(): string | null {
    return localStorage.getItem(this.roleKey);
  }

  isLoggedIn(): boolean {
    return this.hasToken();
  }

  get authenticated(): Observable<boolean> {
    return this.isAuthenticated$.asObservable();
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }
}
