import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { DashboardComponent } from './dashboard.component';
import { DashboardService } from '../../core/services/dashboard.service';
import { Dashboard } from '../../core/models/dashboard.model';

const mockDashboard: Dashboard = {
  projectProgress: 45.5,
  totalSessions: 204,
  completedSessions: 92,
  totalHoursPlanned: 680,
  totalHoursSpent: 306,
  activeSprint: {
    id: 2, sprintNumber: 2, name: 'Módulos Core', nameEn: 'Core Modules',
    description: '', weeks: 4, totalHours: 130, totalSessions: 39,
    startDate: '2026-02-16', endDate: '2026-03-14', focus: 'Core',
    color: '#3884F4', status: 'ACTIVE', actualHours: 40,
    completedSessions: 12, completionNotes: '', taskCount: 39, progressPercent: 30
  },
  todayTask: {
    id: 1, sprintId: 1, sprintNumber: 1, sprintName: 'Fundação',
    taskCode: 'S1-01', sessionDate: '2026-02-03', dayOfWeek: 'SEG',
    weekNumber: 1, plannedHours: 3.5, title: 'Setup do Projecto',
    titleEn: 'Project Setup', description: '', deliverables: ['pom.xml', 'docker-compose.yml'],
    validationCriteria: ['mvn clean install'], coverageTarget: '80%',
    status: 'PLANNED', actualHours: 0, startedAt: '', completedAt: '',
    completionNotes: '', blockers: '', sortOrder: 1, notes: [], executions: []
  },
  recentTasks: [],
  sprintSummaries: [
    { sprintNumber: 1, name: 'Fundação', progress: 100, status: 'COMPLETED', color: '#2EA043' },
    { sprintNumber: 2, name: 'Core', progress: 30, status: 'ACTIVE', color: '#3884F4' }
  ],
  upcomingBlockedDays: [],
  weekProgress: { weekTasks: 5, weekCompleted: 3, weekHoursPlanned: 17.5, weekHoursSpent: 10 }
};

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let mockDashboardService: { getDeveloperDashboard: jest.Mock };

  beforeEach(async () => {
    mockDashboardService = {
      getDeveloperDashboard: jest.fn().mockReturnValue(of(mockDashboard))
    };

    await TestBed.configureTestingModule({
      imports: [DashboardComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: DashboardService, useValue: mockDashboardService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have null dashboard initially', () => {
    expect(component.dashboard).toBeNull();
  });

  it('should call getDeveloperDashboard on init', () => {
    fixture.detectChanges();
    expect(mockDashboardService.getDeveloperDashboard).toHaveBeenCalled();
  });

  it('should populate dashboard data after init', () => {
    fixture.detectChanges();
    expect(component.dashboard).toBe(mockDashboard);
  });

  it('should return 0 for hoursPercent when dashboard is null', () => {
    expect(component.hoursPercent).toBe(0);
  });

  it('should calculate hoursPercent correctly', () => {
    fixture.detectChanges();
    const expected = (306 * 100) / 680;
    expect(component.hoursPercent).toBe(expected);
  });

  it('should return 0 for hoursPercent when totalHoursPlanned is 0', () => {
    component.dashboard = { ...mockDashboard, totalHoursPlanned: 0 };
    expect(component.hoursPercent).toBe(0);
  });

  it('should return 0 for weekPercent when dashboard is null', () => {
    expect(component.weekPercent).toBe(0);
  });

  it('should calculate weekPercent correctly', () => {
    fixture.detectChanges();
    const expected = (3 * 100) / 5;
    expect(component.weekPercent).toBe(expected);
  });

  it('should return 0 for weekPercent when weekTasks is 0', () => {
    component.dashboard = {
      ...mockDashboard,
      weekProgress: { weekTasks: 0, weekCompleted: 0, weekHoursPlanned: 0, weekHoursSpent: 0 }
    };
    expect(component.weekPercent).toBe(0);
  });

  it('should return 0 for weekPercent when weekProgress is falsy', () => {
    component.dashboard = { ...mockDashboard, weekProgress: null as any };
    expect(component.weekPercent).toBe(0);
  });
});
