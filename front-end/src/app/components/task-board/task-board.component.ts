import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';

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

  tasks: Task[] = [];

  newTask: Task = {
    title: '',
    description: '',
    status: 'TODO',
    priority: 'MEDIUM',
    projectId: 0
  };

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    if (this.projectId) {
      this.loadTasks();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['projectId'] && !changes['projectId'].firstChange) {
      this.loadTasks();
    }
  }

  loadTasks(): void {
    this.taskService.getTasksByProject(this.projectId).subscribe({
      next: (data) => (this.tasks = data),
      error: (err) => console.error('Error loading tasks:', err)
    });
  }

  createTask(): void {

    // Αν ο τίτλος είναι κενός, η συνάρτηση σταματά αμέσως
    if (!this.newTask.title.trim()) return; 

    this.newTask.projectId = this.projectId;

    this.taskService.createTask(this.newTask).subscribe({
      next: (createdTask) => {
        this.tasks.push(createdTask);
        this.resetNewTaskForm();
      },
      error: (err) => console.error('Error creating task:', err)
    });
  }

  // Αλλαγή κατάστασης ενός Task (π.χ. μετακίνηση σε DONE)
  updateTaskStatus(task: Task, newStatus: 'TODO' | 'IN_PROGRESS' | 'DONE'): void {
    if (!task.id) return;

    // Αντιγραφή των πεδίων του task και αντικατάσταση (overwrite) του status με το νέο
    const updatedTask: Task = { ...task, status: newStatus };

    this.taskService.updateTask(task.id, updatedTask).subscribe({
      next: (res) => {
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
          this.tasks = this.tasks.filter(t => t.id !== id);
        },
        error: (err) => console.error('Error deleting task:', err)
      });
    }
  }

  get todoTasks(): Task[] {
    return this.tasks.filter(t => t.status === 'TODO');
  }

  get inProgressTasks(): Task[] {
    return this.tasks.filter(t => t.status === 'IN_PROGRESS');
  }

  get doneTasks(): Task[] {
    return this.tasks.filter(t => t.status === 'DONE');
  }

  private resetNewTaskForm(): void {
    this.newTask = {
      title: '',
      description: '',
      status: 'TODO',
      priority: 'MEDIUM',
      projectId: this.projectId
    };
  }

}
