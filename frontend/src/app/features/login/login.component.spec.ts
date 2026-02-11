import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../core/services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: { login: jest.Mock };
  let router: Router;

  beforeEach(async () => {
    mockAuthService = {
      login: jest.fn().mockReturnValue(of({ token: 'test-token', role: 'DEVELOPER', expiresIn: 3600 }))
    };

    await TestBed.configureTestingModule({
      imports: [LoginComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: mockAuthService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have empty initial state', () => {
    expect(component.username).toBe('');
    expect(component.password).toBe('');
    expect(component.error).toBe('');
    expect(component.loading).toBe(false);
  });

  it('should not call auth.login if username is empty', () => {
    component.username = '';
    component.password = 'admin123';
    component.login();
    expect(mockAuthService.login).not.toHaveBeenCalled();
    expect(component.loading).toBe(false);
  });

  it('should not call auth.login if password is empty', () => {
    component.username = 'admin';
    component.password = '';
    component.login();
    expect(mockAuthService.login).not.toHaveBeenCalled();
    expect(component.loading).toBe(false);
  });

  it('should not call auth.login if both username and password are empty', () => {
    component.username = '';
    component.password = '';
    component.login();
    expect(mockAuthService.login).not.toHaveBeenCalled();
  });

  it('should call auth.login with credentials on valid form', () => {
    component.username = 'admin';
    component.password = 'admin123';
    component.login();
    expect(mockAuthService.login).toHaveBeenCalledWith('admin', 'admin123');
  });

  it('should set loading to true when login starts', () => {
    component.username = 'admin';
    component.password = 'admin123';
    component.login();
    expect(component.loading).toBe(true);
  });

  it('should clear error when login starts', () => {
    component.error = 'Previous error';
    component.username = 'admin';
    component.password = 'admin123';
    component.login();
    expect(component.error).toBe('');
  });

  it('should navigate to / on successful login', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');
    component.username = 'admin';
    component.password = 'admin123';
    component.login();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });

  it('should set error message on login failure', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('401')));
    component.username = 'admin';
    component.password = 'wrong';
    component.login();
    expect(component.error).toBe('Credenciais inválidas');
    expect(component.loading).toBe(false);
  });
});
