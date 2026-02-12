import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ApiService } from '../../core/services/api.service';
import { Prompt } from '../../core/models/task.model';

@Component({
  selector: 'app-prompt-generator',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatSnackBarModule],
  template: `
    <h2>Gerador de Prompts</h2>

    @if (prompt) {
      <mat-card class="prompt-card">
        <div class="prompt-header">
          <div>
            <span class="task-code">{{ prompt.taskCode }}</span>
            <span class="task-title">{{ prompt.title }}</span>
          </div>
          <button mat-raised-button class="btn-gold" (click)="copy()">
            <mat-icon>content_copy</mat-icon> Copiar Prompt
          </button>
        </div>
        <pre class="prompt-text">{{ prompt.prompt }}</pre>
      </mat-card>
    } @else {
      <mat-card>
        <p>A carregar prompt de hoje...</p>
      </mat-card>
    }
  `,
  styles: [`
    .prompt-card { padding: 24px; background: var(--angola-black); color: white; }
    .prompt-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .task-code { font-weight: 700; font-size: 18px; color: var(--angola-gold); margin-right: 12px; }
    .task-title { font-size: 16px; }
    .prompt-text {
      white-space: pre-wrap; font-size: 13px; line-height: 1.6;
      font-family: 'Source Code Pro', 'Courier New', monospace;
      padding: 16px; background: rgba(255,255,255,0.05); border-radius: 8px;
    }
  `]
})
export class PromptGeneratorComponent implements OnInit {
  prompt: Prompt | null = null;

  constructor(private api: ApiService, private snackBar: MatSnackBar, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.api.get<Prompt>('/prompts/today').subscribe(p => { this.prompt = p; this.cdr.markForCheck(); });
  }

  copy(): void {
    if (!this.prompt) return;
    navigator.clipboard.writeText(this.prompt.prompt).then(() => {
      this.snackBar.open('Prompt copiado!', 'OK', { duration: 2000 });
    });
  }
}
