export type TaskStatus = 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'BLOCKED' | 'SKIPPED';

export interface Task {
  id: number;
  sprintId: number;
  sprintNumber: number;
  sprintName: string;
  taskCode: string;
  sessionDate: string;
  dayOfWeek: string;
  weekNumber: number;
  plannedHours: number;
  title: string;
  titleEn: string;
  description: string;
  deliverables: string[];
  validationCriteria: string[];
  coverageTarget: string;
  status: TaskStatus;
  actualHours: number;
  startedAt: string;
  completedAt: string;
  completionNotes: string;
  blockers: string;
  sortOrder: number;
  notes: TaskNote[];
  executions: TaskExecution[];
}

export interface TaskNote {
  id: number;
  taskId: number;
  noteType: 'INFO' | 'WARNING' | 'BLOCKER' | 'DECISION' | 'OBSERVATION';
  content: string;
  author: string;
  createdAt: string;
}

export interface TaskExecution {
  id: number;
  taskId: number;
  startedAt: string;
  endedAt: string;
  hoursSpent: number;
  promptUsed: string;
  responseSummary: string;
  notes: string;
}

export interface Prompt {
  taskId: number;
  taskCode: string;
  title: string;
  prompt: string;
}
