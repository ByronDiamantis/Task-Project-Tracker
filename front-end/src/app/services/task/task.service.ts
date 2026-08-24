import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskRequest, TaskResponse } from '../../models/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  createTask(request: TaskRequest): Observable<TaskResponse> {
    return this.http.post<TaskResponse>(this.apiUrl, request);
  }

  getTasksByAssignee(assigneeId: number): Observable<TaskResponse[]> {
    return this.http.get<TaskResponse[]>(`${this.apiUrl}/assignee/${assigneeId}`);
  }

  getTasksByProject(projectId: number): Observable<TaskResponse[]> {
    return this.http.get<TaskResponse[]>(`${this.apiUrl}/project/${projectId}`);
  }

  getTaskById(taskId: number): Observable<TaskResponse> {
    return this.http.get<TaskResponse>(`${this.apiUrl}/${taskId}`);
  }

  updateTask(taskId: number, request: TaskRequest): Observable<TaskResponse> {
    return this.http.put<TaskResponse>(`${this.apiUrl}/${taskId}`, request);
  }

  deleteTask(taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${taskId}`);
  }

}
