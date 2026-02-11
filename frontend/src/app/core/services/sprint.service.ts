import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { Sprint, SprintProgress } from '../models/sprint.model';

@Injectable({ providedIn: 'root' })
export class SprintService {
  constructor(private api: ApiService) {}

  findAll(): Observable<Sprint[]> {
    return this.api.get<Sprint[]>('/sprints');
  }

  findById(id: number): Observable<Sprint> {
    return this.api.get<Sprint>(`/sprints/${id}`);
  }

  findActive(): Observable<Sprint> {
    return this.api.get<Sprint>('/sprints/active');
  }

  getProgress(id: number): Observable<SprintProgress> {
    return this.api.get<SprintProgress>(`/sprints/${id}/progress`);
  }

  update(id: number, data: Partial<Sprint>): Observable<Sprint> {
    return this.api.patch<Sprint>(`/sprints/${id}`, data);
  }
}
