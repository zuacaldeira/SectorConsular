import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { SprintListComponent } from './sprint-list.component';
import { SprintService } from '../../core/services/sprint.service';
import { Sprint } from '../../core/models/sprint.model';

const mockSprints: Sprint[] = [
  {
    id: 1, sprintNumber: 1, name: 'Fundação', nameEn: 'Foundation',
    description: 'Setup inicial', weeks: 3, totalHours: 100, totalSessions: 30,
    startDate: '2026-02-03', endDate: '2026-02-21', focus: 'Setup',
    color: '#2EA043', status: 'COMPLETED', actualHours: 100,
    completedSessions: 30, completionNotes: '', taskCount: 30, progressPercent: 100
  },
  {
    id: 2, sprintNumber: 2, name: 'Módulos Core', nameEn: 'Core Modules',
    description: 'Core modules', weeks: 4, totalHours: 130, totalSessions: 39,
    startDate: '2026-02-24', endDate: '2026-03-21', focus: 'Core',
    color: '#3884F4', status: 'ACTIVE', actualHours: 40,
    completedSessions: 12, completionNotes: '', taskCount: 39, progressPercent: 30
  }
];

describe('SprintListComponent', () => {
  let component: SprintListComponent;
  let fixture: ComponentFixture<SprintListComponent>;
  let mockSprintService: { findAll: jest.Mock };

  beforeEach(async () => {
    mockSprintService = {
      findAll: jest.fn().mockReturnValue(of(mockSprints))
    };

    await TestBed.configureTestingModule({
      imports: [SprintListComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: SprintService, useValue: mockSprintService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SprintListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have empty sprints array initially', () => {
    expect(component.sprints).toEqual([]);
  });

  it('should call sprintService.findAll() on init', () => {
    fixture.detectChanges();
    expect(mockSprintService.findAll).toHaveBeenCalled();
  });

  it('should populate sprints after init', () => {
    fixture.detectChanges();
    expect(component.sprints).toBe(mockSprints);
    expect(component.sprints.length).toBe(2);
  });

  it('should render sprint cards after init', () => {
    fixture.detectChanges();
    const sprintCards = fixture.nativeElement.querySelectorAll('.sprint-card');
    expect(sprintCards.length).toBe(2);
  });

  it('should render the page heading', () => {
    fixture.detectChanges();
    const heading = fixture.nativeElement.querySelector('h2');
    expect(heading.textContent).toBe('Sprints');
  });
});
