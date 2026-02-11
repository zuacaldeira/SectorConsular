export interface Sprint {
  id: number;
  sprintNumber: number;
  name: string;
  nameEn: string;
  description: string;
  weeks: number;
  totalHours: number;
  totalSessions: number;
  startDate: string;
  endDate: string;
  focus: string;
  color: string;
  status: 'PLANNED' | 'ACTIVE' | 'COMPLETED';
  actualHours: number;
  completedSessions: number;
  completionNotes: string;
  taskCount: number;
  progressPercent: number;
}

export interface SprintProgress {
  sprintNumber: number;
  name: string;
  totalSessions: number;
  completedSessions: number;
  totalHours: number;
  actualHours: number;
  progressPercent: number;
  plannedTasks: number;
  inProgressTasks: number;
  completedTasks: number;
  blockedTasks: number;
  skippedTasks: number;
}
