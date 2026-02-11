import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { TaskListComponent } from './task-list.component';
import { TaskService } from '../../core/services/task.service';

const mockPage = {
  content: [
    {
      id: 1, sprintId: 1, sprintNumber: 1, sprintName: 'Fundação',
      taskCode: 'S1-01', sessionDate: '2026-02-03', dayOfWeek: 'SEG',
      weekNumber: 1, plannedHours: 3.5, title: 'Setup do Projecto',
      titleEn: 'Project Setup', description: '', deliverables: [],
      validationCriteria: [], coverageTarget: '80%', status: 'PLANNED' as const,
      actualHours: 0, startedAt: '', completedAt: '', completionNotes: '',
      blockers: '', sortOrder: 1, notes: [], executions: []
    },
    {
      id: 2, sprintId: 1, sprintNumber: 1, sprintName: 'Fundação',
      taskCode: 'S1-02', sessionDate: '2026-02-04', dayOfWeek: 'TER',
      weekNumber: 1, plannedHours: 3.5, title: 'Esquema da BD',
      titleEn: 'DB Schema', description: '', deliverables: [],
      validationCriteria: [], coverageTarget: '80%', status: 'COMPLETED' as const,
      actualHours: 3, startedAt: '', completedAt: '', completionNotes: '',
      blockers: '', sortOrder: 2, notes: [], executions: []
    }
  ],
  totalElements: 204,
  totalPages: 11,
  size: 20,
  number: 0
};

describe('TaskListComponent', () => {
  let component: TaskListComponent;
  let fixture: ComponentFixture<TaskListComponent>;
  let mockTaskService: { findAll: jest.Mock };

  beforeEach(async () => {
    mockTaskService = {
      findAll: jest.fn().mockReturnValue(of(mockPage))
    };

    await TestBed.configureTestingModule({
      imports: [TaskListComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: TaskService, useValue: mockTaskService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have correct initial state', () => {
    expect(component.tasks).toEqual([]);
    expect(component.filterStatus).toBeNull();
    expect(component.totalElements).toBe(0);
    expect(component.pageSize).toBe(20);
    expect(component.page).toBe(0);
  });

  it('should call load() on init which calls taskService.findAll()', () => {
    fixture.detectChanges();
    expect(mockTaskService.findAll).toHaveBeenCalledWith({ page: 0, size: 20 });
  });

  it('should populate tasks and totalElements after init', () => {
    fixture.detectChanges();
    expect(component.tasks.length).toBe(2);
    expect(component.totalElements).toBe(204);
  });

  it('should include status filter in params when filterStatus is set', () => {
    component.filterStatus = 'COMPLETED';
    component.load();
    expect(mockTaskService.findAll).toHaveBeenCalledWith({ page: 0, size: 20, status: 'COMPLETED' });
  });

  it('should not include status in params when filterStatus is null', () => {
    component.filterStatus = null;
    component.load();
    expect(mockTaskService.findAll).toHaveBeenCalledWith({ page: 0, size: 20 });
  });

  it('should update page and pageSize on onPage and reload', () => {
    fixture.detectChanges();
    mockTaskService.findAll.mockClear();

    component.onPage({ pageIndex: 2, pageSize: 50, length: 204 });

    expect(component.page).toBe(2);
    expect(component.pageSize).toBe(50);
    expect(mockTaskService.findAll).toHaveBeenCalledWith({ page: 2, size: 50 });
  });

  it('should render the page heading', () => {
    fixture.detectChanges();
    const heading = fixture.nativeElement.querySelector('h2');
    expect(heading.textContent).toBe('Tarefas');
  });
});
