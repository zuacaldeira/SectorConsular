import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  template: `<span class="badge" [class]="'badge-' + status.toLowerCase()">{{ label }}</span>`,
  styles: [`
    .badge {
      display: inline-block;
      padding: 2px 10px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 600;
      text-transform: uppercase;
    }
    .badge-planned { background: #E8EBED; color: #57606A; }
    .badge-active, .badge-in_progress { background: #DBEAFE; color: #3884F4; }
    .badge-completed { background: #D1FAE5; color: #2EA043; }
    .badge-blocked { background: #FEE2E2; color: #CC092F; }
    .badge-skipped { background: #F3F4F6; color: #8B949E; }
  `]
})
export class StatusBadgeComponent {
  @Input() status = 'PLANNED';
  get label(): string {
    const labels: Record<string, string> = {
      'PLANNED': 'Planeado',
      'ACTIVE': 'Activo',
      'IN_PROGRESS': 'Em Progresso',
      'COMPLETED': 'Concluído',
      'BLOCKED': 'Bloqueado',
      'SKIPPED': 'Ignorado'
    };
    return labels[this.status] || this.status;
  }
}
