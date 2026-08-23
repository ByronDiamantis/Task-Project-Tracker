import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService } from '../../services/task/task.service';
import { UserService } from '../../services/user/user.service';
import { TaskRequest, TaskResponse, TaskPriority,TaskStatus } from '../../models/task';

@Component({
  selector: 'app-task-board',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-board.component.html',
  styleUrl: './task-board.component.scss'
})
export class TaskBoardComponent implements OnInit, OnChanges {
  // Παίρνουμε το ID του επιλεγμένου Project από το γονικό component
  @Input() projectId!: number;

  tasks: TaskResponse[] = []; // Χρήση του TaskResponse model για τα δεδομένα που λαμβάνουμε
  users: any[] = []; // Λίστα χρηστών για το dropdown της ανάθεσης

  // Χρήση του TaskRequest model για τη φόρμα δημιουργίας
  newTask: TaskRequest = {
    title: '',
    description: '',
    status: TaskStatus.TODO,
    priority: TaskPriority.MEDIUM,
    projectId: 0,
    assigneeId: undefined
  };

  constructor(
    private taskService: TaskService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    if (this.projectId) {
      this.loadTasks();
    }
    this.loadUsers();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['projectId'] && !changes['projectId'].firstChange) {
      this.loadTasks();
    }
  }

  loadTasks(): void {
    this.taskService.getTasksByProject(this.projectId).subscribe({
      next: (data: TaskResponse[]) => (this.tasks = data),
      error: (err) => console.error('Error loading tasks:', err)
    });
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => (this.users = data),
      error: (err) => console.error('Error loading users:', err)
    });
  }

  createTask(): void {
    if (!this.newTask.title.trim()) return;

    this.newTask.projectId = this.projectId;

    this.taskService.createTask(this.newTask).subscribe({
      next: (createdTask: TaskResponse) => {
        this.tasks.push(createdTask);
        this.resetNewTaskForm();
      },
      error: (err) => console.error('Error creating task:', err)
    });
  }

  // Αλλαγή τύπου παραμέτρου σε: TaskStatus | 'TODO' | 'IN_PROGRESS' | 'DONE'
  updateTaskStatus(task: TaskResponse, newStatus: TaskStatus | 'TODO' | 'IN_PROGRESS' | 'DONE'): void {
    if (!task.id) return;

    const updateRequest: TaskRequest = {
      title: task.title,
      description: task.description,
      status: newStatus as TaskStatus,
      priority: task.priority,
      dueDate: task.dueDate,
      projectId: task.projectId,
      assigneeId: task.assignee?.id
    };

    this.taskService.updateTask(task.id, updateRequest).subscribe({
      next: (res: TaskResponse) => {
        task.status = res.status;
      },
      error: (err) => console.error('Error updating task status:', err)
    });
  }

  deleteTask(id?: number): void {
    if (!id) return;

    if (confirm('Θέλετε να διαγράψετε αυτό το task;')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.tasks = this.tasks.filter((t) => t.id !== id);
        },
        error: (err) => console.error('Error deleting task:', err)
      });
    }
  }

  // Getters για το Kanban Board χρησιμοποιώντας τα TaskStatus Enums
  get todoTasks(): TaskResponse[] {
    return this.tasks.filter((t) => t.status === TaskStatus.TODO);
  }

  get inProgressTasks(): TaskResponse[] {
    return this.tasks.filter((t) => t.status === TaskStatus.IN_PROGRESS);
  }

  get doneTasks(): TaskResponse[] {
    return this.tasks.filter((t) => t.status === TaskStatus.DONE);
  }

  private resetNewTaskForm(): void {
    this.newTask = {
      title: '',
      description: '',
      status: TaskStatus.TODO,
      priority: TaskPriority.MEDIUM,
      projectId: this.projectId,
      assigneeId: undefined
    };
  }
}
