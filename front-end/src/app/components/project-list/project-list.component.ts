import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../services/project.service';
import { UserService } from '../../services/user.service';
import { Project } from '../../models/project';
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
  projects: Project[] = [];
  currentUser: User | null = null;
  selectedProjectId: number | null = null;
  isCreating: boolean = false;

  newProject: Project = {
    title: '',
    description: '',
    ownerId: 0
  };

  constructor(
    private projectService: ProjectService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    if (this.currentUser && this.currentUser.id) {
      this.newProject.ownerId = this.currentUser.id;
      this.loadProjects(this.currentUser.id);
    } 
  }

  loadProjects(ownerId: number): void {
    this.projectService.getProjectsByOwner(ownerId).subscribe({
      next: (data) => (this.projects = data),
      error: (err) => console.error('Error fetching projects', err)
    });
  }

  createProject(): void {
    this.currentUser = this.userService.getCurrentUser();
    const userId = this.currentUser?.id || (this.currentUser as any)?.userId;

    if (!this.newProject.title?.trim()) {
      alert('Παρακαλώ συμπλήρωσε τον τίτλο του project.');
      return;
    }

    if (!userId) {
      alert('Δεν βρέθηκε ID συνδεδεμένου χρήστη. Κάνε ξανά login!');
      return;
    }

    // Δημιουργία payload βάσει του ProjectRequest DTO της Java
    const payload = {
      name: this.newProject.title.trim(), // 👈 Το backend περιμένει 'name' αντί για 'title'
      description: this.newProject.description ? this.newProject.description.trim() : '',
      ownerId: Number(userId)             // 👈 Το backend περιμένει 'ownerId' ως αριθμό
    };

    console.log('Sending correct DTO payload:', payload);

    this.projectService.createProject(payload as any).subscribe({
      next: (createdProject) => {
        console.log('Project created successfully:', createdProject);
        this.projects.push(createdProject);
        this.newProject = { title: '', description: '', ownerId: userId };
      },
      error: (err) => {
        console.error('Error creating project:', err);
        alert('Σφάλμα κατά τη δημιουργία project. Δες την κονσόλα.');
      }
    });
  }

  deleteProject(id?: number, event?: Event): void {
    if (event) {
      event.stopPropagation(); // Αποφεύγουμε το άνοιγμα του project όταν πατάμε διαγραφή
    }
    if (!id) return;

    if(confirm('Είσαι σίγουρος ότι θέλεις να διαγράψεις αυτό το Project;')) {
      this.projectService.deleteProject(id).subscribe({
        next: () => {
          this.projects = this.projects.filter(p => p.id !== id);
          if (this.selectedProjectId === id) {
            this.selectedProjectId = null;
          }
        },
        error: (err) => console.error('Error deleting project', err)
      });
    }
  }

  selectProject(id?:number): void {
    if (id) {
      this.selectedProjectId = id;
    }
  }

  backToProjects(): void {
    this.selectedProjectId = null;
  }
}
