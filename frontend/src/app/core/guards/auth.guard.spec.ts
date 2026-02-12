import { TestBed } from '@angular/core/testing';
import { Router, UrlTree } from '@angular/router';
import { authGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('authGuard', () => {
  let authService: jest.Mocked<AuthService>;
  let router: jest.Mocked<Router>;
  const mockUrlTree = {} as UrlTree;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: AuthService,
          useValue: {
            isLoggedIn: jest.fn(),
            getToken: jest.fn(),
            isTokenExpired: jest.fn().mockReturnValue(false),
            getRefreshToken: jest.fn().mockReturnValue(null),
            refreshTokens: jest.fn(),
          },
        },
        {
          provide: Router,
          useValue: {
            createUrlTree: jest.fn().mockReturnValue(mockUrlTree),
          },
        },
      ],
    });

    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    router = TestBed.inject(Router) as jest.Mocked<Router>;
  });

  it('should return true when user is logged in', () => {
    authService.isLoggedIn.mockReturnValue(true);

    const result = TestBed.runInInjectionContext(() =>
      authGuard({} as any, {} as any)
    );

    expect(result).toBe(true);
  });

  it('should return UrlTree to /login when user is not logged in', () => {
    authService.isLoggedIn.mockReturnValue(false);

    const result = TestBed.runInInjectionContext(() =>
      authGuard({} as any, {} as any)
    );

    expect(result).toBe(mockUrlTree);
    expect(router.createUrlTree).toHaveBeenCalledWith(['/login']);
  });

  it('should call authService.isLoggedIn to check authentication', () => {
    authService.isLoggedIn.mockReturnValue(true);

    TestBed.runInInjectionContext(() =>
      authGuard({} as any, {} as any)
    );

    expect(authService.isLoggedIn).toHaveBeenCalled();
  });

  it('should not call router.createUrlTree when user is logged in', () => {
    authService.isLoggedIn.mockReturnValue(true);

    TestBed.runInInjectionContext(() =>
      authGuard({} as any, {} as any)
    );

    expect(router.createUrlTree).not.toHaveBeenCalled();
  });

  it('should redirect to /login path specifically', () => {
    authService.isLoggedIn.mockReturnValue(false);

    TestBed.runInInjectionContext(() =>
      authGuard({} as any, {} as any)
    );

    expect(router.createUrlTree).toHaveBeenCalledTimes(1);
    expect(router.createUrlTree).toHaveBeenCalledWith(['/login']);
  });
});
