import { Component, Input } from '@angular/core';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-progress-bar',
  standalone: true,
  imports: [DecimalPipe],
  template: `
    <div class="progress-container">
      <div class="progress-track">
        <div class="progress-fill" [style.width.%]="value" [style.background]="color"></div>
      </div>
      @if (showLabel) {
        <span class="progress-label">{{ value | number:'1.0-1' }}%</span>
      }
    </div>
  `,
  styles: [`
    .progress-container { display: flex; align-items: center; gap: 8px; }
    .progress-track {
      flex: 1;
      height: 8px;
      background: var(--border-light);
      border-radius: 4px;
      overflow: hidden;
    }
    .progress-fill {
      height: 100%;
      border-radius: 4px;
      transition: width 0.3s ease;
    }
    .progress-label { font-size: 13px; font-weight: 600; color: var(--text-secondary); min-width: 40px; }
  `]
})
export class ProgressBarComponent {
  @Input() value = 0;
  @Input() color = 'var(--color-blue)';
  @Input() showLabel = true;
}
