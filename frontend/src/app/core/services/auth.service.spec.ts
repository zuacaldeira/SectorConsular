import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { ApiService } from './api.service';

describe('AuthService', () => {
  let service: AuthService;
  let apiService: jest.Mocked<ApiService>;
  let router: jest.Mocked<Router>;

  beforeEach(() => {
    localStorage.clear();

    const apiMock = {
      get: jest.fn().mockReturnValue(of({})),
      post: jest.fn().mockReturnValue(of({})),
      patch: jest.fn().mockReturnValue(of({})),
      put: jest.fn().mockReturnValue(of({})),
      delete: jest.fn().mockReturnValue(of({})),
    };

    const routerMock = {
      navigate: jest.fn(),
    };

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        { provide: ApiService, useValue: apiMock },
        { provide: Router, useValue: routerMock },
      ]
    });

    service = TestBed.inject(AuthService);
    apiService = TestBed.inject(ApiService) as jest.Mocked<ApiService>;
    router = TestBed.inject(Router) as jest.Mocked<Router>;
  });

  afterEach(() => {
    localStorage.clear();
  });

  describe('login', () => {
    it('should call api.post with /auth/login and credentials', () => {
      const mockResponse = { token: 'jwt-token-123', role: 'DEVELOPER', expiresIn: 3600 };
      apiService.post.mockReturnValue(of(mockResponse));

      service.login('admin', 'admin123').subscribe();

      expect(apiService.post).toHaveBeenCalledWith('/auth/login', {
        username: 'admin',
        password: 'admin123'
      });
    });

    it('should store token in localStorage on successful login', () => {
      const mockResponse = { token: 'jwt-token-123', role: 'DEVELOPER', expiresIn: 3600 };
      apiService.post.mockReturnValue(of(mockResponse));

      service.login('admin', 'admin123').subscribe();

      expect(localStorage.getItem('sgcd_pm_token')).toBe('jwt-token-123');
    });

    it('should store role in localStorage on successful login', () => {
      const mockResponse = { token: 'jwt-token-123', role: 'DEVELOPER', expiresIn: 3600 };
      apiService.post.mockReturnValue(of(mockResponse));

      service.login('admin', 'admin123').subscribe();

      expect(localStorage.getItem('sgcd_pm_role')).toBe('DEVELOPER');
    });

    it('should emit true on isAuthenticated$ after successful login', (done) => {
      const mockResponse = { token: 'jwt-token-123', role: 'DEVELOPER', expiresIn: 3600 };
      apiService.post.mockReturnValue(of(mockResponse));

      service.login('admin', 'admin123').subscribe();

      service.authenticated.subscribe(isAuth => {
        expect(isAuth).toBe(true);
        done();
      });
    });

    it('should handle stakeholder login correctly', () => {
      const mockResponse = { token: 'stakeholder-token', role: 'STAKEHOLDER', expiresIn: 3600 };
      apiService.post.mockReturnValue(of(mockResponse));

      service.login('stakeholder', 'stakeholder2026').subscribe();

      expect(localStorage.getItem('sgcd_pm_token')).toBe('stakeholder-token');
      expect(localStorage.getItem('sgcd_pm_role')).toBe('STAKEHOLDER');
    });
  });

  describe('logout', () => {
    beforeEach(() => {
      localStorage.setItem('sgcd_pm_token', 'some-token');
      localStorage.setItem('sgcd_pm_role', 'DEVELOPER');
    });

    it('should remove token from localStorage', () => {
      service.logout();
      expect(localStorage.getItem('sgcd_pm_token')).toBeNull();
    });

    it('should remove role from localStorage', () => {
      service.logout();
      expect(localStorage.getItem('sgcd_pm_role')).toBeNull();
    });

    it('should emit false on isAuthenticated$', (done) => {
      service.logout();

      service.authenticated.subscribe(isAuth => {
        expect(isAuth).toBe(false);
        done();
      });
    });

    it('should navigate to /login', () => {
      service.logout();
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  describe('getToken', () => {
    it('should return token from localStorage when present', () => {
      localStorage.setItem('sgcd_pm_token', 'my-token');
      expect(service.getToken()).toBe('my-token');
    });

    it('should return null when no token in localStorage', () => {
      expect(service.getToken()).toBeNull();
    });
  });

  describe('getRole', () => {
    it('should return role from localStorage when present', () => {
      localStorage.setItem('sgcd_pm_role', 'DEVELOPER');
      expect(service.getRole()).toBe('DEVELOPER');
    });

    it('should return null when no role in localStorage', () => {
      expect(service.getRole()).toBeNull();
    });
  });

  describe('isLoggedIn', () => {
    it('should return true when token exists in localStorage', () => {
      // Need a valid JWT with exp in the future for isTokenExpired() check
      const payload = btoa(JSON.stringify({ exp: Math.floor(Date.now() / 1000) + 3600 }));
      localStorage.setItem('sgcd_pm_token', `h.${payload}.s`);
      expect(service.isLoggedIn()).toBe(true);
    });

    it('should return false when no token in localStorage', () => {
      expect(service.isLoggedIn()).toBe(false);
    });
  });

  describe('authenticated', () => {
    it('should return an observable', () => {
      expect(service.authenticated).toBeDefined();
      expect(typeof service.authenticated.subscribe).toBe('function');
    });

    it('should initially emit false when no token is stored', (done) => {
      service.authenticated.subscribe(isAuth => {
        expect(isAuth).toBe(false);
        done();
      });
    });

    it('should emit true after login and false after logout', () => {
      const emissions: boolean[] = [];
      const mockResponse = { token: 'jwt-token', role: 'DEVELOPER', expiresIn: 3600 };
      apiService.post.mockReturnValue(of(mockResponse));

      service.authenticated.subscribe(val => emissions.push(val));

      // Initial state is false (no token)
      expect(emissions[0]).toBe(false);

      // After login
      service.login('admin', 'admin123').subscribe();
      expect(emissions[1]).toBe(true);

      // After logout
      service.logout();
      expect(emissions[2]).toBe(false);
    });
  });
});
