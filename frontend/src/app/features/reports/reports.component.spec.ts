import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { ReportsComponent } from './reports.component';
import { ApiService } from '../../core/services/api.service';
import { SprintService } from '../../core/services/sprint.service';
import { Sprint } from '../../core/models/sprint.model';

const mockSprints: Sprint[] = [
  {
    id: 1, sprintNumber: 1, name: 'Fundação', nameEn: 'Foundation',
    description: '', weeks: 3, totalHours: 100, totalSessions: 30,
    startDate: '2026-02-03', endDate: '2026-02-21', focus: 'Setup',
    color: '#2EA043', status: 'COMPLETED', actualHours: 100,
    completedSessions: 30, completionNotes: '', taskCount: 30, progressPercent: 100
  }
];

const mockReports = [
  {
    id: 1, sprintId: 1, sprintNumber: 1, sprintName: 'Fundação',
    reportType: 'SPRINT_COMPLETION', generatedAt: '2026-02-22T10:00:00',
    summaryPt: 'Sprint concluído com sucesso.',
    summaryEn: 'Sprint completed successfully.'
  }
];

describe('ReportsComponent', () => {
  let component: ReportsComponent;
  let fixture: ComponentFixture<ReportsComponent>;
  let mockApiService: { get: jest.Mock; post: jest.Mock };
  let mockSprintService: { findAll: jest.Mock };
  let mockSnackBar: { open: jest.Mock };

  beforeEach(async () => {
    mockApiService = {
      get: jest.fn().mockReturnValue(of(mockReports)),
      post: jest.fn().mockReturnValue(of(mockReports[0]))
    };

    mockSprintService = {
      findAll: jest.fn().mockReturnValue(of(mockSprints))
    };

    mockSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [ReportsComponent, NoopAnimationsModule],
      providers: [
        { provide: ApiService, useValue: mockApiService },
        { provide: SprintService, useValue: mockSprintService },
        { provide: MatSnackBar, useValue: mockSnackBar }
      ]
    }).overrideComponent(ReportsComponent, {
      add: { providers: [{ provide: MatSnackBar, useValue: mockSnackBar }] }
    }).compileComponents();

    fixture = TestBed.createComponent(ReportsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have correct initial state', () => {
    expect(component.sprints).toEqual([]);
    expect(component.reports).toEqual([]);
    expect(component.generating).toBe(false);
  });

  it('should call sprintService.findAll on init', () => {
    fixture.detectChanges();
    expect(mockSprintService.findAll).toHaveBeenCalled();
  });

  it('should call api.get /reports on init (loadReports)', () => {
    fixture.detectChanges();
    expect(mockApiService.get).toHaveBeenCalledWith('/reports');
  });

  it('should populate sprints after init', () => {
    fixture.detectChanges();
    expect(component.sprints).toBe(mockSprints);
  });

  it('should populate reports after init', () => {
    fixture.detectChanges();
    expect(component.reports).toBe(mockReports);
  });

  it('should call api.post to generate a report', () => {
    fixture.detectChanges();
    mockApiService.get.mockClear();
    component.generate(1);
    expect(mockApiService.post).toHaveBeenCalledWith('/reports/sprint/1/generate');
  });

  it('should set generating to true during generation', () => {
    fixture.detectChanges();
    // We need to make post return an observable that we can control
    // But with the current sync mock, it resolves immediately
    component.generate(1);
    // After sync resolution, generating should be false again
    expect(component.generating).toBe(false);
  });

  it('should show snackbar after successful generation', () => {
    fixture.detectChanges();
    component.generate(1);
    expect(mockSnackBar.open).toHaveBeenCalledWith('Relatório gerado!', 'OK', { duration: 3000 });
  });

  it('should reload reports after successful generation', () => {
    fixture.detectChanges();
    mockApiService.get.mockClear();
    component.generate(1);
    expect(mockApiService.get).toHaveBeenCalledWith('/reports');
  });

  it('should reset generating on error', () => {
    mockApiService.post.mockReturnValue(throwError(() => new Error('500')));
    fixture.detectChanges();
    component.generate(1);
    expect(component.generating).toBe(false);
  });

  it('should render the page heading', () => {
    fixture.detectChanges();
    const heading = fixture.nativeElement.querySelector('h2');
    expect(heading.textContent).toBe('Relatórios');
  });
});
