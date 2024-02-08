import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {UsersComponent} from "./components/users/users.component";
import {tokenGuard} from "./guards/token.guard";
import {LoginComponent} from "./components/login/login.component";
import {CreateUserComponent} from "./components/users/create-user/create-user.component";
import {createGuard} from "./guards/create.guard";
import {UpdateUserComponent} from "./components/users/update-user/update-user.component";
import {updateGuard} from "./guards/update.guard";
import {VacuumsComponent} from "./components/vacuums/vacuums.component";
import {vaccumSearchGuard} from "./guards/vaccum-search.guard";
import {vacuumCreateGuard} from "./guards/vacuum-create.guard";
import {CreateVacuumComponent} from "./components/vacuums/create-vacuum/create-vacuum.component";
import {ScheduleVacuumComponent} from "./components/vacuums/schedule-vacuum/schedule-vacuum.component";
import {scheduleVacuumGuard} from "./guards/schedule-vacuum.guard";
import {ErrorsComponent} from "./errors/errors.component";

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'users',
    component: UsersComponent,
    canActivate: [tokenGuard],
  },
  {
    path: 'create-user',
    component: CreateUserComponent,
    canActivate: [createGuard],
  },
  {
    path: 'update-user/:id',
    component: UpdateUserComponent,
    canActivate: [updateGuard],
  },
  {
    path: 'vacuums',
    component: VacuumsComponent,
    canActivate: [vaccumSearchGuard],
  },
  {
    path: 'create-vacuum',
    component: CreateVacuumComponent,
    canActivate: [vacuumCreateGuard],
  },
  {
    path: 'schedule-vacuum/:id',
    component: ScheduleVacuumComponent,
    canActivate: [scheduleVacuumGuard],
  },
  {
    path: 'errors',
    component: ErrorsComponent,
    canActivate: [tokenGuard],
  }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
