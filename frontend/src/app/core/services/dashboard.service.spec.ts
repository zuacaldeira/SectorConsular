import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { DashboardService } from './dashboard.service';
import { ApiService } from './api.service';

describe('DashboardService', () => {
  let service: DashboardService;
  let apiService: jest.Mocked<ApiService>;

  const mockDashboard = {
    projectProgress: 15.5,
    totalSessions: 204,
    completedSessions: 32,
    totalHoursPlanned: 680,
    totalHoursSpent: 105,
    activeSprint: { id: 1, sprintNumber: 1, name: 'Fundacao' },
    todayTask: { id: 33, taskCode: 'S2-003' },
    recentTasks: [],
    sprintSummaries: [],
    upcomingBlockedDays: [],
    weekProgress: { weekTasks: 5, weekCompleted: 3, weekHoursPlanned: 17.5, weekHoursSpent: 10.5 },
  };

  const mockStakeholderDashboard = {
    projectName: 'SGCD MVP',
    client: 'Embaixada de Angola',
    overallProgress: 15.5,
    totalSessions: 204,
    completedSessions: 32,
    totalHoursPlanned: 680,
    totalHoursSpent: 105,
    startDate: '2026-02-09',
    targetDate: '2026-07-31',
    daysRemaining: 120,
    sprints: [],
    milestones: [],
    weeklyActivity: { sessionsThisWeek: 3, hoursThisWeek: 10.5, tasksCompletedThisWeek: 3 },
    lastUpdated: '2026-02-11T10:00:00',
  };

  const mockCalendarData = {
    year: 2026,
    month: 2,
    days: [
      {
        date: '2026-02-09',
        dayOfWeek: 'SUNDAY',
        isBlocked: false,
        blockReason: '',
        task: null,
        isWorkDay: true,
      }
    ],
  };

  const mockBlockedDays = [
    {
      id: 1,
      blockedDate: '2026-02-17',
      dayOfWeek: 'TUESDAY',
      blockType: 'HOLIDAY' as const,
      reason: 'Carnaval',
      hoursLost: 3.5,
    },
  ];

  beforeEach(() => {
    const apiMock = {
      get: jest.fn().mockReturnValue(of({})),
      post: jest.fn().mockReturnValue(of({})),
      patch: jest.fn().mockReturnValue(of({})),
      put: jest.fn().mockReturnValue(of({})),
      delete: jest.fn().mockReturnValue(of({})),
    };

    TestBed.configureTestingModule({
      providers: [
        DashboardService,
        { provide: ApiService, useValue: apiMock },
      ]
    });

    service = TestBed.inject(DashboardService);
    apiService = TestBed.inject(ApiService) as jest.Mocked<ApiService>;
  });

  describe('getDeveloperDashboard', () => {
    it('should call api.get with /dashboard', () => {
      apiService.get.mockReturnValue(of(mockDashboard));

      service.getDeveloperDashboard().subscribe(result => {
        expect(result).toEqual(mockDashboard);
      });

      expect(apiService.get).toHaveBeenCalledWith('/dashboard');
    });
  });

  describe('getStakeholderDashboard', () => {
    it('should call api.get with /dashboard/stakeholder', () => {
      apiService.get.mockReturnValue(of(mockStakeholderDashboard));

      service.getStakeholderDashboard().subscribe(result => {
        expect(result).toEqual(mockStakeholderDashboard);
      });

      expect(apiService.get).toHaveBeenCalledWith('/dashboard/stakeholder');
    });
  });

  describe('getCalendar', () => {
    it('should call api.get with /calendar and year/month params', () => {
      apiService.get.mockReturnValue(of(mockCalendarData));

      service.getCalendar(2026, 2).subscribe(result => {
        expect(result).toEqual(mockCalendarData);
      });

      expect(apiService.get).toHaveBeenCalledWith('/calendar', { year: 2026, month: 2 });
    });

    it('should call api.get with /calendar and undefined params when not provided', () => {
      apiService.get.mockReturnValue(of(mockCalendarData));

      service.getCalendar().subscribe(result => {
        expect(result).toEqual(mockCalendarData);
      });

      expect(apiService.get).toHaveBeenCalledWith('/calendar', { year: undefined, month: undefined });
    });

    it('should call api.get with /calendar and partial params', () => {
      apiService.get.mockReturnValue(of(mockCalendarData));

      service.getCalendar(2026).subscribe();

      expect(apiService.get).toHaveBeenCalledWith('/calendar', { year: 2026, month: undefined });
    });
  });

  describe('getBlockedDays', () => {
    it('should call api.get with /calendar/blocked', () => {
      apiService.get.mockReturnValue(of(mockBlockedDays));

      service.getBlockedDays().subscribe(result => {
        expect(result).toEqual(mockBlockedDays);
      });

      expect(apiService.get).toHaveBeenCalledWith('/calendar/blocked');
    });

    it('should return array of blocked days', () => {
      apiService.get.mockReturnValue(of(mockBlockedDays));

      service.getBlockedDays().subscribe(result => {
        expect(Array.isArray(result)).toBe(true);
        expect(result.length).toBe(1);
        expect(result[0].blockType).toBe('HOLIDAY');
      });
    });
  });
});
