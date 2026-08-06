import { Task } from './task';

export interface Project {
    id?: number;
    title: string;
    description: string;
    ownerId: number;
    ownerName?: string;
    tasks?: Task[]
}
