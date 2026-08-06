import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../models/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  getProjectsByOwner(ownerId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/owner/${ownerId}`);
  }

  getProjectById(id: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/${id}`);
  }

  createProject(project: Project): Observable<Project[]> {
    return this.http.post<Project[]>(this.apiUrl, project);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
