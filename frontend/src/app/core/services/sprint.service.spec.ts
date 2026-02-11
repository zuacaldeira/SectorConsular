import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { SprintService } from './sprint.service';
import { ApiService } from './api.service';
import { Sprint, SprintProgress } from '../models/sprint.model';

describe('SprintService', () => {
  let service: SprintService;
  let apiService: jest.Mocked<ApiService>;

  const mockSprint: Sprint = {
    id: 1,
    sprintNumber: 1,
    name: 'Fundacao',
    nameEn: 'Foundation',
    description: 'Sprint de fundacao',
    weeks: 3,
    totalHours: 102,
    totalSessions: 30,
    startDate: '2026-02-09',
    endDate: '2026-02-28',
    focus: 'Base architecture',
    color: '#CC092F',
    status: 'ACTIVE',
    actualHours: 24,
    completedSessions: 8,
    completionNotes: '',
    taskCount: 30,
    progressPercent: 26.7,
  };

  const mockSprintProgress: SprintProgress = {
    sprintNumber: 1,
    name: 'Fundacao',
    totalSessions: 30,
    completedSessions: 8,
    totalHours: 102,
    actualHours: 24,
    progressPercent: 26.7,
    plannedTasks: 15,
    inProgressTasks: 1,
    completedTasks: 8,
    blockedTasks: 0,
    skippedTasks: 0,
  };

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
        SprintService,
        { provide: ApiService, useValue: apiMock },
      ]
    });

    service = TestBed.inject(SprintService);
    apiService = TestBed.inject(ApiService) as jest.Mocked<ApiService>;
  });

  describe('findAll', () => {
    it('should call api.get with /sprints', () => {
      const mockSprints = [mockSprint];
      apiService.get.mockReturnValue(of(mockSprints));

      service.findAll().subscribe(result => {
        expect(result).toEqual(mockSprints);
      });

      expect(apiService.get).toHaveBeenCalledWith('/sprints');
    });
  });

  describe('findById', () => {
    it('should call api.get with /sprints/:id', () => {
      apiService.get.mockReturnValue(of(mockSprint));

      service.findById(1).subscribe(result => {
        expect(result).toEqual(mockSprint);
      });

      expect(apiService.get).toHaveBeenCalledWith('/sprints/1');
    });

    it('should pass different sprint ids correctly', () => {
      apiService.get.mockReturnValue(of(mockSprint));

      service.findById(5).subscribe();

      expect(apiService.get).toHaveBeenCalledWith('/sprints/5');
    });
  });

  describe('findActive', () => {
    it('should call api.get with /sprints/active', () => {
      apiService.get.mockReturnValue(of(mockSprint));

      service.findActive().subscribe(result => {
        expect(result).toEqual(mockSprint);
      });

      expect(apiService.get).toHaveBeenCalledWith('/sprints/active');
    });
  });

  describe('getProgress', () => {
    it('should call api.get with /sprints/:id/progress', () => {
      apiService.get.mockReturnValue(of(mockSprintProgress));

      service.getProgress(1).subscribe(result => {
        expect(result).toEqual(mockSprintProgress);
      });

      expect(apiService.get).toHaveBeenCalledWith('/sprints/1/progress');
    });

    it('should pass different sprint ids correctly', () => {
      apiService.get.mockReturnValue(of(mockSprintProgress));

      service.getProgress(3).subscribe();

      expect(apiService.get).toHaveBeenCalledWith('/sprints/3/progress');
    });
  });

  describe('update', () => {
    it('should call api.patch with /sprints/:id and data', () => {
      const updateData: Partial<Sprint> = { status: 'COMPLETED', completionNotes: 'Sprint done' };
      const updatedSprint = { ...mockSprint, ...updateData };
      apiService.patch.mockReturnValue(of(updatedSprint));

      service.update(1, updateData).subscribe(result => {
        expect(result).toEqual(updatedSprint);
      });

      expect(apiService.patch).toHaveBeenCalledWith('/sprints/1', updateData);
    });

    it('should pass partial data correctly', () => {
      const updateData: Partial<Sprint> = { actualHours: 50 };
      apiService.patch.mockReturnValue(of({ ...mockSprint, ...updateData }));

      service.update(2, updateData).subscribe();

      expect(apiService.patch).toHaveBeenCalledWith('/sprints/2', updateData);
    });
  });
});
