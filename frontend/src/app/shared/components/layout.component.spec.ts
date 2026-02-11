import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LayoutComponent } from './layout.component';
import { AuthService } from '../../core/services/auth.service';

describe('LayoutComponent', () => {
  let component: LayoutComponent;
  let fixture: ComponentFixture<LayoutComponent>;
  let mockAuthService: Partial<AuthService>;

  beforeEach(async () => {
    mockAuthService = {
      getRole: jest.fn().mockReturnValue('DEVELOPER'),
      logout: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [LayoutComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: mockAuthService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LayoutComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have AuthService injected', () => {
    expect(component.auth).toBeTruthy();
    expect(component.auth).toBe(mockAuthService);
  });

  it('should call auth.getRole()', () => {
    fixture.detectChanges();
    expect(mockAuthService.getRole).toHaveBeenCalled();
  });

  it('should render the SGCD-PM logo text', () => {
    fixture.detectChanges();
    const logo = fixture.nativeElement.querySelector('.logo');
    expect(logo).toBeTruthy();
    expect(logo.textContent).toBe('SGCD-PM');
  });

  it('should render navigation links in the sidebar', () => {
    fixture.detectChanges();
    const links = fixture.nativeElement.querySelectorAll('.sidebar a');
    expect(links.length).toBeGreaterThanOrEqual(7);
  });

  it('should contain a router-outlet', () => {
    fixture.detectChanges();
    const outlet = fixture.nativeElement.querySelector('router-outlet');
    expect(outlet).toBeTruthy();
  });
});
