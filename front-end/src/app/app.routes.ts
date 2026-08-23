import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { ProjectListComponent } from './components/project-list/project-list.component';
import { AuthGuard, UnauthGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: AuthComponent, canActivate: [UnauthGuard.canActivate] },
  { path: 'projects', component: ProjectListComponent, canActivate: [AuthGuard.canActivate] },
  { path: '**', redirectTo: 'login' }
];