import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { PromptGeneratorComponent } from './prompt-generator.component';
import { ApiService } from '../../core/services/api.service';
import { Prompt } from '../../core/models/task.model';

const mockPrompt: Prompt = {
  taskId: 10,
  taskCode: 'S1-10',
  title: 'Implementar Serviços',
  prompt: 'Implement the Sprint and Task services with full CRUD operations.'
};

describe('PromptGeneratorComponent', () => {
  let component: PromptGeneratorComponent;
  let fixture: ComponentFixture<PromptGeneratorComponent>;
  let mockApiService: { get: jest.Mock };
  let mockSnackBar: { open: jest.Mock };

  beforeEach(async () => {
    mockApiService = {
      get: jest.fn().mockReturnValue(of(mockPrompt))
    };

    mockSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [PromptGeneratorComponent, NoopAnimationsModule],
      providers: [
        { provide: ApiService, useValue: mockApiService },
        { provide: MatSnackBar, useValue: mockSnackBar }
      ]
    }).overrideComponent(PromptGeneratorComponent, {
      add: { providers: [{ provide: MatSnackBar, useValue: mockSnackBar }] }
    }).compileComponents();

    fixture = TestBed.createComponent(PromptGeneratorComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have null prompt initially', () => {
    expect(component.prompt).toBeNull();
  });

  it('should call api.get with /prompts/today on init', () => {
    fixture.detectChanges();
    expect(mockApiService.get).toHaveBeenCalledWith('/prompts/today');
  });

  it('should populate prompt after init', () => {
    fixture.detectChanges();
    expect(component.prompt).toBe(mockPrompt);
  });

  it('should copy prompt text to clipboard and show snackbar', async () => {
    fixture.detectChanges();
    const writeTextMock = jest.fn().mockResolvedValue(undefined);
    Object.assign(navigator, { clipboard: { writeText: writeTextMock } });

    component.copy();

    expect(writeTextMock).toHaveBeenCalledWith(mockPrompt.prompt);
    await writeTextMock.mock.results[0].value;
    expect(mockSnackBar.open).toHaveBeenCalledWith('Prompt copiado!', 'OK', { duration: 2000 });
  });

  it('should not copy if prompt is null', () => {
    component.prompt = null;
    const writeTextMock = jest.fn().mockResolvedValue(undefined);
    Object.assign(navigator, { clipboard: { writeText: writeTextMock } });

    component.copy();

    expect(writeTextMock).not.toHaveBeenCalled();
  });

  it('should render the heading', () => {
    fixture.detectChanges();
    const heading = fixture.nativeElement.querySelector('h2');
    expect(heading.textContent).toBe('Gerador de Prompts');
  });

  it('should render prompt card when prompt is loaded', () => {
    fixture.detectChanges();
    const card = fixture.nativeElement.querySelector('.prompt-card');
    expect(card).toBeTruthy();
    const taskCode = fixture.nativeElement.querySelector('.task-code');
    expect(taskCode.textContent).toBe('S1-10');
  });
});
