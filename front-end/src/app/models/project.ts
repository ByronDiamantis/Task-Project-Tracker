import { TaskResponse } from "./task";

export interface ProjectRequest {
  name: string;        // Στον Request περιμένει 'name'
  description?: string;
  ownerId: number;
}

export interface ProjectResponse {
  id: number;
  title: string;       // Στον Response επιστρέφει 'title'
  description: string;
  ownerId: number;
  ownerName: string;
  tasks: TaskResponse[];
}