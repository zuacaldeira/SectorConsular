import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { SprintDetailComponent } from './sprint-detail.component';
import { SprintService } from '../../core/services/sprint.service';
import { TaskService } from '../../core/services/task.service';
import { Sprint, SprintProgress } from '../../core/models/sprint.model';

const mockSprint: Sprint = {
  id: 1, sprintNumber: 1, name: 'Fundação', nameEn: 'Foundation',
  description: 'Setup inicial do projecto', weeks: 3, totalHours: 100,
  totalSessions: 30, startDate: '2026-02-03', endDate: '2026-02-21',
  focus: 'Setup', color: '#2EA043', status: 'ACTIVE', actualHours: 40,
  completedSessions: 12, completionNotes: '', taskCount: 30, progressPercent: 40
};

const mockProgress: SprintProgress = {
  sprintNumber: 1, name: 'Fundação', totalSessions: 30,
  completedSessions: 12, totalHours: 100, actualHours: 40,
  progressPercent: 40, plannedTasks: 10, inProgressTasks: 3,
  completedTasks: 12, blockedTasks: 2, skippedTasks: 3
};

const mockTasks = [
  {
    id: 1, sprintId: 1, sprintNumber: 1, sprintName: 'Fundação',
    taskCode: 'S1-01', sessionDate: '2026-02-03', dayOfWeek: 'SEG',
    weekNumber: 1, plannedHours: 3.5, title: 'Setup do Projecto',
    titleEn: 'Project Setup', description: '', deliverables: [],
    validationCriteria: [], coverageTarget: '80%', status: 'COMPLETED' as const,
    actualHours: 3, startedAt: '', completedAt: '', completionNotes: '',
    blockers: '', sortOrder: 1, notes: [], executions: []
  }
];

describe('SprintDetailComponent', () => {
  let component: SprintDetailComponent;
  let fixture: ComponentFixture<SprintDetailComponent>;
  let mockSprintService: { findById: jest.Mock; getProgress: jest.Mock };
  let mockTaskService: { findAll: jest.Mock };

  beforeEach(async () => {
    mockSprintService = {
      findById: jest.fn().mockReturnValue(of(mockSprint)),
      getProgress: jest.fn().mockReturnValue(of(mockProgress))
    };

    mockTaskService = {
      findAll: jest.fn().mockReturnValue(of({ content: mockTasks, totalElements: 1, totalPages: 1, size: 100, number: 0 }))
    };

    await TestBed.configureTestingModule({
      imports: [SprintDetailComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '1' } } }
        },
        { provide: SprintService, useValue: mockSprintService },
        { provide: TaskService, useValue: mockTaskService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SprintDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have null sprint initially', () => {
    expect(component.sprint).toBeNull();
    expect(component.progress).toBeNull();
    expect(component.tasks).toEqual([]);
  });

  it('should get id from route and call sprintService.findById on init', () => {
    fixture.detectChanges();
    expect(mockSprintService.findById).toHaveBeenCalledWith(1);
  });

  it('should call sprintService.getProgress on init', () => {
    fixture.detectChanges();
    expect(mockSprintService.getProgress).toHaveBeenCalledWith(1);
  });

  it('should call taskService.findAll with sprint id on init', () => {
    fixture.detectChanges();
    expect(mockTaskService.findAll).toHaveBeenCalledWith({ sprint: 1, size: 100 });
  });

  it('should populate sprint after init', () => {
    fixture.detectChanges();
    expect(component.sprint).toBe(mockSprint);
  });

  it('should populate progress after init', () => {
    fixture.detectChanges();
    expect(component.progress).toBe(mockProgress);
  });

  it('should populate tasks after init', () => {
    fixture.detectChanges();
    expect(component.tasks).toEqual(mockTasks);
    expect(component.tasks.length).toBe(1);
  });
});
