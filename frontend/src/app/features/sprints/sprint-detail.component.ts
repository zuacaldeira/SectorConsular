import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { SprintService } from '../../core/services/sprint.service';
import { TaskService } from '../../core/services/task.service';
import { Sprint, SprintProgress } from '../../core/models/sprint.model';
import { Task } from '../../core/models/task.model';
import { StatusBadgeComponent } from '../../shared/components/status-badge.component';
import { ProgressBarComponent } from '../../shared/components/progress-bar.component';
import { DatePtPipe } from '../../shared/pipes/date-pt.pipe';
import { HoursPipe } from '../../shared/pipes/hours.pipe';

@Component({
  selector: 'app-sprint-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, MatCardModule, MatButtonModule, MatIconModule,
    StatusBadgeComponent, ProgressBarComponent, DatePtPipe, HoursPipe],
  template: `
    <a mat-button routerLink="/sprints"><mat-icon>arrow_back</mat-icon> Voltar</a>

    @if (sprint) {
      <mat-card class="sprint-header-card" [style.border-left-color]="sprint.color">
        <div class="header-row">
          <h2>Sprint {{ sprint.sprintNumber }}: {{ sprint.name }}</h2>
          <app-status-badge [status]="sprint.status" />
        </div>
        <p class="desc">{{ sprint.description }}</p>
        <div class="meta-row">
          <span>{{ sprint.startDate | datePt }} — {{ sprint.endDate | datePt }}</span>
          <span>{{ sprint.totalSessions }} sessões · {{ sprint.totalHours | hours }}</span>
          <span>Foco: {{ sprint.focus }}</span>
        </div>
        <app-progress-bar [value]="sprint.progressPercent || 0" [color]="sprint.color" />
      </mat-card>

      @if (progress) {
        <div class="stats-row">
          <mat-card class="stat"><div class="stat-val">{{ progress.completedTasks }}</div><div class="stat-lbl">Concluídas</div></mat-card>
          <mat-card class="stat"><div class="stat-val">{{ progress.inProgressTasks }}</div><div class="stat-lbl">Em Progresso</div></mat-card>
          <mat-card class="stat"><div class="stat-val">{{ progress.blockedTasks }}</div><div class="stat-lbl">Bloqueadas</div></mat-card>
          <mat-card class="stat"><div class="stat-val">{{ progress.plannedTasks }}</div><div class="stat-lbl">Planeadas</div></mat-card>
        </div>
      }

      <h3>Tarefas</h3>
      <div class="task-list">
        @for (task of tasks; track task.id) {
          <a class="task-row" [routerLink]="['/tasks', task.id]">
            <app-status-badge [status]="task.status" />
            <span class="code">{{ task.taskCode }}</span>
            <span class="title">{{ task.title }}</span>
            <span class="date">{{ task.sessionDate | datePt }}</span>
            <span class="day">{{ task.dayOfWeek }}</span>
            <span class="hours">{{ task.plannedHours | hours }}</span>
          </a>
        }
      </div>
    }
  `,
  styles: [`
    .sprint-header-card { padding: 20px; border-left: 4px solid; margin: 12px 0 20px; }
    .header-row { display: flex; justify-content: space-between; align-items: center; }
    .header-row h2 { margin: 0; }
    .desc { color: var(--text-secondary); margin: 8px 0; }
    .meta-row { display: flex; gap: 24px; font-size: 13px; color: var(--text-muted); margin-bottom: 12px; }
    .stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; margin-bottom: 20px; }
    .stat { padding: 16px; text-align: center; }
    .stat-val { font-size: 28px; font-weight: 700; }
    .stat-lbl { font-size: 12px; color: var(--text-muted); text-transform: uppercase; }
    .task-list { display: flex; flex-direction: column; gap: 2px; }
    .task-row {
      display: flex; align-items: center; gap: 12px; padding: 10px 12px;
      text-decoration: none; color: inherit; border-radius: 6px; transition: background 0.2s;
    }
    .task-row:hover { background: var(--surface); }
    .code { font-weight: 700; min-width: 55px; }
    .title { flex: 1; }
    .date, .day, .hours { font-size: 13px; color: var(--text-muted); }
  `]
})
export class SprintDetailComponent implements OnInit {
  sprint: Sprint | null = null;
  progress: SprintProgress | null = null;
  tasks: Task[] = [];

  constructor(
    private route: ActivatedRoute,
    private sprintService: SprintService,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.sprintService.findById(id).subscribe(s => this.sprint = s);
    this.sprintService.getProgress(id).subscribe(p => this.progress = p);
    this.taskService.findAll({ sprint: id, size: 100 }).subscribe(page => this.tasks = page.content);
  }
}
