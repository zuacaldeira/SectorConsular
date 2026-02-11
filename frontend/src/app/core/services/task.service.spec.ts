import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { TaskService } from './task.service';
import { ApiService } from './api.service';

describe('TaskService', () => {
  let service: TaskService;
  let apiService: jest.Mocked<ApiService>;

  const mockTask = {
    id: 1,
    sprintId: 1,
    sprintNumber: 1,
    sprintName: 'Fundacao',
    taskCode: 'S1-001',
    sessionDate: '2026-02-09',
    dayOfWeek: 'SUNDAY',
    weekNumber: 1,
    plannedHours: 3.5,
    title: 'Infraestrutura do Projecto',
    titleEn: 'Project Infrastructure',
    description: 'Setup inicial do projecto',
    deliverables: ['pom.xml configurado', 'docker-compose.yml'],
    validationCriteria: ['mvn clean install sem erros'],
    coverageTarget: '80%',
    status: 'PLANNED' as const,
    actualHours: 0,
    startedAt: '',
    completedAt: '',
    completionNotes: '',
    blockers: '',
    sortOrder: 1,
    notes: [],
    executions: [],
  };

  const mockPrompt = {
    taskId: 1,
    taskCode: 'S1-001',
    title: 'Infraestrutura do Projecto',
    prompt: 'Implement the project infrastructure...',
  };

  const mockNote = {
    id: 1,
    taskId: 1,
    noteType: 'INFO' as const,
    content: 'Test note content',
    author: 'admin',
    createdAt: '2026-02-09T10:00:00',
  };

  const mockPage = {
    content: [mockTask],
    totalElements: 1,
    totalPages: 1,
    size: 20,
    number: 0,
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
        TaskService,
        { provide: ApiService, useValue: apiMock },
      ]
    });

    service = TestBed.inject(TaskService);
    apiService = TestBed.inject(ApiService) as jest.Mocked<ApiService>;
  });

  describe('findAll', () => {
    it('should call api.get with /tasks and no params when none provided', () => {
      apiService.get.mockReturnValue(of(mockPage));

      service.findAll().subscribe(result => {
        expect(result).toEqual(mockPage);
      });

      expect(apiService.get).toHaveBeenCalledWith('/tasks', undefined);
    });

    it('should call api.get with /tasks and params when provided', () => {
      const params = { page: 0, size: 20, sprintId: 1, status: 'PLANNED' };
      apiService.get.mockReturnValue(of(mockPage));

      service.findAll(params).subscribe(result => {
        expect(result).toEqual(mockPage);
      });

      expect(apiService.get).toHaveBeenCalledWith('/tasks', params);
    });
  });

  describe('findById', () => {
    it('should call api.get with /tasks/:id', () => {
      apiService.get.mockReturnValue(of(mockTask));

      service.findById(1).subscribe(result => {
        expect(result).toEqual(mockTask);
      });

      expect(apiService.get).toHaveBeenCalledWith('/tasks/1');
    });

    it('should pass different task ids correctly', () => {
      apiService.get.mockReturnValue(of(mockTask));

      service.findById(42).subscribe();

      expect(apiService.get).toHaveBeenCalledWith('/tasks/42');
    });
  });

  describe('findToday', () => {
    it('should call api.get with /tasks/today', () => {
      apiService.get.mockReturnValue(of(mockTask));

      service.findToday().subscribe(result => {
        expect(result).toEqual(mockTask);
      });

      expect(apiService.get).toHaveBeenCalledWith('/tasks/today');
    });
  });

  describe('findNext', () => {
    it('should call api.get with /tasks/next', () => {
      apiService.get.mockReturnValue(of(mockTask));

      service.findNext().subscribe(result => {
        expect(result).toEqual(mockTask);
      });

      expect(apiService.get).toHaveBeenCalledWith('/tasks/next');
    });
  });

  describe('update', () => {
    it('should call api.patch with /tasks/:id and data', () => {
      const updateData = { completionNotes: 'Done', actualHours: 3.0 };
      const updatedTask = { ...mockTask, ...updateData };
      apiService.patch.mockReturnValue(of(updatedTask));

      service.update(1, updateData).subscribe(result => {
        expect(result).toEqual(updatedTask);
      });

      expect(apiService.patch).toHaveBeenCalledWith('/tasks/1', updateData);
    });
  });

  describe('start', () => {
    it('should call api.post with /tasks/:id/start', () => {
      const startedTask = { ...mockTask, status: 'IN_PROGRESS' as const };
      apiService.post.mockReturnValue(of(startedTask));

      service.start(1).subscribe(result => {
        expect(result).toEqual(startedTask);
      });

      expect(apiService.post).toHaveBeenCalledWith('/tasks/1/start');
    });

    it('should pass different task ids correctly', () => {
      apiService.post.mockReturnValue(of(mockTask));

      service.start(15).subscribe();

      expect(apiService.post).toHaveBeenCalledWith('/tasks/15/start');
    });
  });

  describe('complete', () => {
    it('should call api.post with /tasks/:id/complete and data', () => {
      const completionData = { actualHours: 3.5, completionNotes: 'All deliverables met' };
      const completedTask = { ...mockTask, status: 'COMPLETED' as const, ...completionData };
      apiService.post.mockReturnValue(of(completedTask));

      service.complete(1, completionData).subscribe(result => {
        expect(result).toEqual(completedTask);
      });

      expect(apiService.post).toHaveBeenCalledWith('/tasks/1/complete', completionData);
    });

    it('should call api.post with /tasks/:id/complete without data', () => {
      apiService.post.mockReturnValue(of(mockTask));

      service.complete(1).subscribe();

      expect(apiService.post).toHaveBeenCalledWith('/tasks/1/complete', undefined);
    });
  });

  describe('block', () => {
    it('should call api.post with /tasks/:id/block and reason object', () => {
      const blockedTask = { ...mockTask, status: 'BLOCKED' as const, blockers: 'Dependency not ready' };
      apiService.post.mockReturnValue(of(blockedTask));

      service.block(1, 'Dependency not ready').subscribe(result => {
        expect(result).toEqual(blockedTask);
      });

      expect(apiService.post).toHaveBeenCalledWith('/tasks/1/block', { reason: 'Dependency not ready' });
    });
  });

  describe('skip', () => {
    it('should call api.post with /tasks/:id/skip', () => {
      const skippedTask = { ...mockTask, status: 'SKIPPED' as const };
      apiService.post.mockReturnValue(of(skippedTask));

      service.skip(1).subscribe(result => {
        expect(result).toEqual(skippedTask);
      });

      expect(apiService.post).toHaveBeenCalledWith('/tasks/1/skip');
    });

    it('should pass different task ids correctly', () => {
      apiService.post.mockReturnValue(of(mockTask));

      service.skip(100).subscribe();

      expect(apiService.post).toHaveBeenCalledWith('/tasks/100/skip');
    });
  });

  describe('getPrompt', () => {
    it('should call api.get with /tasks/:id/prompt', () => {
      apiService.get.mockReturnValue(of(mockPrompt));

      service.getPrompt(1).subscribe(result => {
        expect(result).toEqual(mockPrompt);
      });

      expect(apiService.get).toHaveBeenCalledWith('/tasks/1/prompt');
    });
  });

  describe('addNote', () => {
    it('should call api.post with /tasks/:id/notes and note data', () => {
      const noteData = { noteType: 'INFO' as const, content: 'Test note' };
      apiService.post.mockReturnValue(of(mockNote));

      service.addNote(1, noteData).subscribe(result => {
        expect(result).toEqual(mockNote);
      });

      expect(apiService.post).toHaveBeenCalledWith('/tasks/1/notes', noteData);
    });

    it('should pass different task ids and note types correctly', () => {
      const noteData = { noteType: 'BLOCKER' as const, content: 'Blocked by external API' };
      apiService.post.mockReturnValue(of(mockNote));

      service.addNote(25, noteData).subscribe();

      expect(apiService.post).toHaveBeenCalledWith('/tasks/25/notes', noteData);
    });
  });
});
