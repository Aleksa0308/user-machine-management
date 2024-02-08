import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ButtonModule} from "primeng/button";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {HttpClientModule} from "@angular/common/http";
import { UsersComponent } from './components/users/users.component';
import { LoginComponent } from './components/login/login.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastModule} from "primeng/toast";
import {ToastrModule} from "ngx-toastr";
import {CookieService} from "ngx-cookie-service";
import {TableModule} from "primeng/table";
import {ContextMenuModule} from "primeng/contextmenu";
import { CreateUserComponent } from './components/users/create-user/create-user.component';
import {MultiSelectModule} from "primeng/multiselect";
import { UpdateUserComponent } from './components/users/update-user/update-user.component';
import {RippleModule} from "primeng/ripple";
import {MenubarModule} from "primeng/menubar";
import { VacuumsComponent } from './components/vacuums/vacuums.component';
import {CheckboxModule} from "primeng/checkbox";
import {DropdownModule} from "primeng/dropdown";
import { CreateVacuumComponent } from './components/vacuums/create-vacuum/create-vacuum.component';
import { ScheduleVacuumComponent } from './components/vacuums/schedule-vacuum/schedule-vacuum.component';
import {CalendarModule} from "primeng/calendar";
import { ErrorsComponent } from './errors/errors.component';


@NgModule({
  declarations: [
    AppComponent,
    UsersComponent,
    LoginComponent,
    CreateUserComponent,
    UpdateUserComponent,
    VacuumsComponent,
    CreateVacuumComponent,
    ScheduleVacuumComponent,
    ErrorsComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ButtonModule,
    FormsModule,
    HttpClientModule,
    InputTextModule,
    ToastrModule.forRoot(),
    TableModule,
    ContextMenuModule,
    MultiSelectModule,
    RippleModule,
    MenubarModule,
    CheckboxModule,
    DropdownModule,
    CalendarModule,
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
