import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../services/project/project.service';
import { UserService } from '../../services/user/user.service';
import { ProjectRequest, ProjectResponse } from '../../models/project';
import { User } from '../../models/user';
import { TaskBoardComponent } from '../task-board/task-board.component'; 
import { Router } from '@angular/router';


@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TaskBoardComponent],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss'
})

export class ProjectListComponent implements OnInit {
  projects: ProjectResponse[] = [];
  selectedProjectId: number | null = null;

  newProject = {
    title: '',
    description: ''
  };

  constructor(
    private projectService: ProjectService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const user = this.userService.getCurrentUser();
    const ownerId = user?.id || (user as any)?.userId;
    if (ownerId) {
      this.loadProjects(ownerId);
    }
  }

  loadProjects(ownerId: number): void {
    this.projectService.getProjectsByOwner(ownerId).subscribe({
      next: (data) => (this.projects = data),
      error: (err) => console.error('Error fetching projects:', err)
    });
  }

  createProject(): void {
    const user = this.userService.getCurrentUser();
    const ownerId = user?.id || (user as any)?.userId;

    if (!this.newProject.title.trim() || !ownerId) return;

    const payload: ProjectRequest = {
      name: this.newProject.title.trim(),
      description: this.newProject.description.trim(),
      ownerId: Number(ownerId)
    };

    this.projectService.createProject(payload).subscribe({
      next: (createdProject) => {
        this.projects.push(createdProject);
        this.newProject = { title: '', description: '' };
      },
      error: (err) => console.error('Error creating project:', err)
    });
  }

  deleteProject(id: number, event: Event): void {
    event.stopPropagation();
    if (confirm('Θέλετε να διαγράψετε αυτό το Project;')) {
      this.projectService.deleteProject(id).subscribe({
        next: () => {
          this.projects = this.projects.filter((p) => p.id !== id);
          if (this.selectedProjectId === id) this.selectedProjectId = null;
        },
        error: (err) => console.error('Error deleting project:', err)
      });
    }
  }

  selectProject(id: number): void {
    this.selectedProjectId = id;
  }

  backToProjects(): void {
    this.selectedProjectId = null;
  }
}
