import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StatusBadgeComponent } from './status-badge.component';

describe('StatusBadgeComponent', () => {
  let component: StatusBadgeComponent;
  let fixture: ComponentFixture<StatusBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatusBadgeComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(StatusBadgeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have default status PLANNED', () => {
    expect(component.status).toBe('PLANNED');
  });

  it('should return "Planeado" for PLANNED status', () => {
    component.status = 'PLANNED';
    expect(component.label).toBe('Planeado');
  });

  it('should return "Activo" for ACTIVE status', () => {
    component.status = 'ACTIVE';
    expect(component.label).toBe('Activo');
  });

  it('should return "Em Progresso" for IN_PROGRESS status', () => {
    component.status = 'IN_PROGRESS';
    expect(component.label).toBe('Em Progresso');
  });

  it('should return "Concluído" for COMPLETED status', () => {
    component.status = 'COMPLETED';
    expect(component.label).toBe('Concluído');
  });

  it('should return "Bloqueado" for BLOCKED status', () => {
    component.status = 'BLOCKED';
    expect(component.label).toBe('Bloqueado');
  });

  it('should return "Ignorado" for SKIPPED status', () => {
    component.status = 'SKIPPED';
    expect(component.label).toBe('Ignorado');
  });

  it('should return the status itself for an unknown status', () => {
    component.status = 'UNKNOWN_STATUS';
    expect(component.label).toBe('UNKNOWN_STATUS');
  });

  it('should render the badge with the correct CSS class', () => {
    component.status = 'COMPLETED';
    fixture.detectChanges();
    const badgeEl = fixture.nativeElement.querySelector('.badge');
    expect(badgeEl).toBeTruthy();
    expect(badgeEl.classList).toContain('badge-completed');
    expect(badgeEl.textContent.trim()).toBe('Concluído');
  });

  it('should render the default status badge on init', () => {
    fixture.detectChanges();
    const badgeEl = fixture.nativeElement.querySelector('.badge');
    expect(badgeEl.classList).toContain('badge-planned');
    expect(badgeEl.textContent.trim()).toBe('Planeado');
  });
});
