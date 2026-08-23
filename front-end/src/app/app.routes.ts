import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { ProjectListComponent } from './components/project-list/project-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: AuthComponent },
  { path: 'projects', component: ProjectListComponent },
  { path: '**', redirectTo: 'login' }
];