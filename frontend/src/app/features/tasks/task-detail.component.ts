import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TaskService } from '../../core/services/task.service';
import { Task, Prompt } from '../../core/models/task.model';
import { StatusBadgeComponent } from '../../shared/components/status-badge.component';
import { DatePtPipe } from '../../shared/pipes/date-pt.pipe';
import { HoursPipe } from '../../shared/pipes/hours.pipe';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, MatCardModule, MatButtonModule, MatIconModule,
    MatInputModule, MatFormFieldModule, MatSnackBarModule, StatusBadgeComponent, DatePtPipe, HoursPipe],
  template: `
    <a mat-button routerLink="/tasks"><mat-icon>arrow_back</mat-icon> Voltar</a>

    @if (task) {
      <mat-card class="task-header">
        <div class="header-row">
          <span class="task-code">{{ task.taskCode }}</span>
          <app-status-badge [status]="task.status" />
        </div>
        <h2>{{ task.title }}</h2>
        <div class="meta">
          <span>{{ task.sessionDate | datePt:'long' }}</span>
          <span>{{ task.dayOfWeek }}</span>
          <span>{{ task.plannedHours | hours }}</span>
          <span>Semana {{ task.weekNumber }}</span>
          <span>Sprint {{ task.sprintNumber }}: {{ task.sprintName }}</span>
          <span>Cobertura: {{ task.coverageTarget }}</span>
        </div>
      </mat-card>

      <!-- Deliverables -->
      @if (task.deliverables?.length) {
        <mat-card class="section">
          <h3>Entregáveis</h3>
          <ul>
            @for (d of task.deliverables; track d) {
              <li>{{ d }}</li>
            }
          </ul>
        </mat-card>
      }

      <!-- Validation -->
      @if (task.validationCriteria?.length) {
        <mat-card class="section">
          <h3>Validação</h3>
          <ul class="validation">
            @for (v of task.validationCriteria; track v) {
              <li>{{ v }}</li>
            }
          </ul>
        </mat-card>
      }

      <!-- Actions -->
      <div class="actions">
        @if (task.status === 'PLANNED') {
          <button mat-raised-button class="btn-primary" (click)="startTask()">
            <mat-icon>play_arrow</mat-icon> Iniciar Tarefa
          </button>
        }
        @if (task.status === 'IN_PROGRESS') {
          <button mat-raised-button color="primary" (click)="completeTask()">
            <mat-icon>check</mat-icon> Concluir Tarefa
          </button>
        }
        @if (task.status === 'PLANNED' || task.status === 'IN_PROGRESS') {
          <button mat-stroked-button color="warn" (click)="blockTask()">
            <mat-icon>block</mat-icon> Bloquear
          </button>
        }
        <button mat-stroked-button (click)="loadPrompt()">
          <mat-icon>smart_toy</mat-icon> Ver Prompt
        </button>
      </div>

      <!-- Prompt -->
      @if (prompt) {
        <mat-card class="section prompt-section">
          <div class="prompt-header">
            <h3>Prompt Claude</h3>
            <button mat-icon-button (click)="copyPrompt()"><mat-icon>content_copy</mat-icon></button>
          </div>
          <pre class="prompt-text">{{ prompt.prompt }}</pre>
        </mat-card>
      }

      <!-- Notes -->
      <mat-card class="section">
        <h3>Notas</h3>
        @if (task.notes?.length) {
          @for (note of task.notes; track note.id) {
            <div class="note">
              <span class="note-type">{{ note.noteType }}</span>
              <span class="note-content">{{ note.content }}</span>
              <span class="note-date">{{ note.createdAt | datePt }}</span>
            </div>
          }
        }
        <div class="add-note">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Adicionar nota...</mat-label>
            <input matInput [(ngModel)]="newNote" (keyup.enter)="addNote()">
          </mat-form-field>
        </div>
      </mat-card>
    }
  `,
  styles: [`
    .task-header { padding: 20px; margin: 12px 0; }
    .header-row { display: flex; justify-content: space-between; align-items: center; }
    .task-code { font-size: 18px; font-weight: 700; }
    .task-header h2 { margin: 8px 0; }
    .meta { display: flex; flex-wrap: wrap; gap: 16px; font-size: 13px; color: var(--text-secondary); }
    .section { padding: 20px; margin-bottom: 16px; }
    .section h3 { margin: 0 0 12px; }
    .section ul { padding-left: 20px; margin: 0; }
    .section li { margin: 4px 0; }
    .validation li { color: var(--color-green); }
    .actions { display: flex; gap: 12px; margin: 16px 0; }
    .prompt-section { background: var(--angola-black); color: white; }
    .prompt-section h3 { color: var(--angola-gold); }
    .prompt-header { display: flex; justify-content: space-between; align-items: center; }
    .prompt-header button { color: var(--angola-gold); }
    .prompt-text { white-space: pre-wrap; font-size: 13px; line-height: 1.5; font-family: 'Source Code Pro', monospace; }
    .note { display: flex; gap: 8px; padding: 8px 0; border-bottom: 1px solid var(--border-light); font-size: 14px; }
    .note-type { font-weight: 600; min-width: 80px; }
    .note-content { flex: 1; }
    .note-date { color: var(--text-muted); font-size: 12px; }
    .add-note { margin-top: 12px; }
    .full-width { width: 100%; }
  `]
})
export class TaskDetailComponent implements OnInit {
  task: Task | null = null;
  prompt: Prompt | null = null;
  newNote = '';

  constructor(
    private route: ActivatedRoute,
    private taskService: TaskService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadTask();
  }

  loadTask(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.taskService.findById(id).subscribe(t => this.task = t);
  }

  startTask(): void {
    if (!this.task) return;
    this.taskService.start(this.task.id).subscribe(t => {
      this.task = t;
      this.snackBar.open('Tarefa iniciada!', 'OK', { duration: 3000 });
    });
  }

  completeTask(): void {
    if (!this.task) return;
    this.taskService.complete(this.task.id).subscribe(t => {
      this.task = t;
      this.snackBar.open('Tarefa concluída!', 'OK', { duration: 3000 });
    });
  }

  blockTask(): void {
    if (!this.task) return;
    const reason = prompt('Razão do bloqueio:');
    if (!reason) return;
    this.taskService.block(this.task.id, reason).subscribe(t => {
      this.task = t;
      this.snackBar.open('Tarefa bloqueada', 'OK', { duration: 3000 });
    });
  }

  loadPrompt(): void {
    if (!this.task) return;
    this.taskService.getPrompt(this.task.id).subscribe(p => this.prompt = p);
  }

  copyPrompt(): void {
    if (!this.prompt) return;
    navigator.clipboard.writeText(this.prompt.prompt).then(() => {
      this.snackBar.open('Prompt copiado!', 'OK', { duration: 2000 });
    });
  }

  addNote(): void {
    if (!this.task || !this.newNote.trim()) return;
    this.taskService.addNote(this.task.id, { content: this.newNote, noteType: 'INFO' }).subscribe(() => {
      this.newNote = '';
      this.loadTask();
    });
  }
}
