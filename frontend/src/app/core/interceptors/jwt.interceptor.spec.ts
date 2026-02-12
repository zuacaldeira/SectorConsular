import { TestBed } from '@angular/core/testing';
import { HttpRequest, HttpHandlerFn, HttpEvent, HttpResponse } from '@angular/common/http';
import { of, Observable } from 'rxjs';
import { jwtInterceptor } from './jwt.interceptor';
import { AuthService } from '../services/auth.service';

describe('jwtInterceptor', () => {
  let authService: jest.Mocked<AuthService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: AuthService,
          useValue: {
            getToken: jest.fn(),
            isTokenExpired: jest.fn().mockReturnValue(false),
            isTokenExpiringSoon: jest.fn().mockReturnValue(false),
            getRefreshToken: jest.fn().mockReturnValue(null),
            refreshTokens: jest.fn(),
            logout: jest.fn(),
          },
        },
      ],
    });

    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
  });

  it('should add Authorization Bearer header when token exists', () => {
    authService.getToken.mockReturnValue('test-jwt-token');
    const req = new HttpRequest('GET', '/api/test');

    const next: HttpHandlerFn = (interceptedReq) => {
      expect(interceptedReq.headers.get('Authorization')).toBe('Bearer test-jwt-token');
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => jwtInterceptor(req, next));
  });

  it('should not add Authorization header when no token exists', () => {
    authService.getToken.mockReturnValue(null);
    const req = new HttpRequest('GET', '/api/test');

    const next: HttpHandlerFn = (interceptedReq) => {
      expect(interceptedReq.headers.has('Authorization')).toBe(false);
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => jwtInterceptor(req, next));
  });

  it('should pass the request to the next handler', (done) => {
    authService.getToken.mockReturnValue(null);
    const req = new HttpRequest('GET', '/api/test');
    const mockResponse = new HttpResponse({ status: 200, body: { data: 'test' } });

    const next: HttpHandlerFn = () => of(mockResponse);

    TestBed.runInInjectionContext(() => {
      const result$ = jwtInterceptor(req, next) as Observable<HttpEvent<any>>;
      result$.subscribe((response) => {
        expect(response).toBe(mockResponse);
        done();
      });
    });
  });

  it('should call authService.getToken', () => {
    authService.getToken.mockReturnValue(null);
    const req = new HttpRequest('GET', '/api/test');
    const next: HttpHandlerFn = () => of(new HttpResponse({ status: 200 }));

    TestBed.runInInjectionContext(() => jwtInterceptor(req, next));

    expect(authService.getToken).toHaveBeenCalled();
  });

  it('should not modify the original request URL', () => {
    authService.getToken.mockReturnValue('some-token');
    const req = new HttpRequest('GET', '/api/data');

    const next: HttpHandlerFn = (interceptedReq) => {
      expect(interceptedReq.url).toBe('/api/data');
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => jwtInterceptor(req, next));
  });

  it('should not modify the original request method', () => {
    authService.getToken.mockReturnValue('some-token');
    const req = new HttpRequest('POST', '/api/data', { name: 'test' });

    const next: HttpHandlerFn = (interceptedReq) => {
      expect(interceptedReq.method).toBe('POST');
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => jwtInterceptor(req, next));
  });

  it('should preserve existing headers when adding token', () => {
    authService.getToken.mockReturnValue('my-token');
    const { HttpHeaders } = require('@angular/common/http');
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const req = new HttpRequest('GET', '/api/test', { headers });

    const next: HttpHandlerFn = (interceptedReq) => {
      expect(interceptedReq.headers.get('Authorization')).toBe('Bearer my-token');
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => jwtInterceptor(req, next));
  });
});
