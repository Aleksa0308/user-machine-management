import {Component, inject} from '@angular/core';
import {Permission} from "../../../types/user.types";
import {NgForm} from "@angular/forms";
import {UsersService} from "../../../services/users.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent {

  usersService = inject(UsersService);
  router = inject(Router);
  toasterService = inject(ToastrService);
  permissions!: Permission[];
  selectedPermissions!: Permission[];

  ngOnInit(): void {
    this.permissions = [
      {permissionId: 1, name: "can_read_users"},
      {permissionId: 2, name: "can_create_users"},
      {permissionId: 3, name: "can_update_users"},
      {permissionId: 4, name: "can_delete_users"},
      {permissionId: 5, name: "can_search_vacuums"},
      {permissionId: 6, name: "can_start_vacuums"},
      {permissionId: 7, name: "can_stop_vacuums"},
      {permissionId: 8, name: "can_discharge_vacuums"},
      {permissionId: 9, name: "can_create_vacuums"},
      {permissionId: 10, name: "can_destroy_vacuums"},
      {permissionId: 11, name: "can_schedule_vacuums"},
    ];
  }

  onSubmit(form: NgForm){
    const firstname = form.value.firstname;
    const lastname = form.value.lastname;
    const email = form.value.email;
    const password = form.value.password;
    const permissions = form.value.permissions;
    // create new array of permissions just strings
    const permissionsArray = permissions.map((permission: Permission) => permission.name);
    // call the service
    this.usersService.createUser(firstname, lastname, email, password, permissionsArray).subscribe((usersResponse) => {
      this.router.navigate(['/users']);
      //toast success
      this.toasterService.success("User created successfully!");
      }, error => {
        //toast error
        this.toasterService.error("Error creating user!");
      });
    // clear the form
    form.reset();
  }
}
