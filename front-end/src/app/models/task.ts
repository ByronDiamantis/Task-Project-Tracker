import { User } from "./user";

export interface Task {
    id?: number;
    title: string;
    description: string;
    status: 'TODO' | 'IN_PROGRESS' | 'DONE';
    priority?: 'LOW' | 'MEDIUM' | 'HIGH';
    dueDate?: string;
    createdAt?: string;
    projectId: number;
    assignee: User | null;
    assigneeId?: number;
}
