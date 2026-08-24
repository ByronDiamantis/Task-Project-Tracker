import { TaskResponse } from "./task";

export interface ProjectRequest {
  name: string;        
  description?: string;
  ownerId: number;
}

export interface ProjectResponse {
  id: number;
  title: string;       
  description: string;
  ownerId: number;
  ownerName: string;
  tasks: TaskResponse[];
}