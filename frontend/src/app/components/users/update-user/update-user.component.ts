import {Component, inject} from '@angular/core';
import {UsersService} from "../../../services/users.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Permission, User, UserResponse} from "../../../types/user.types";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent {
  usersService = inject(UsersService);
  router = inject(Router);
  toasterService = inject(ToastrService);
  permissions!: Permission[];
  selectedPermissions!: Permission[];

  userId!: string;
  user!: User; // Define User type based on your API response

  constructor(private route: ActivatedRoute) {
    this.userId = this.route.snapshot.paramMap.get('id') ?? '';
  }
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
    // Fetch user details based on the userId
    this.usersService.getUserById(this.userId).subscribe(
      (response: UserResponse) => {
        this.user = response.user; // Adjust the property based on your API response structure
        // Populate the form with the fetched user details
        console.log(this.user.permissions)
        this.selectedPermissions = this.user.permissions;
      },
      (error) => {
        console.error("Error fetching user details", error);
      }
    );
  }


  onSubmit(form: NgForm){
    const { firstname, lastname, email } = form.value;
    const permissions = this.selectedPermissions.map((permission) => permission.name);
    this.usersService.updateUser(this.userId, firstname, lastname, email, permissions).subscribe(
      () => {
        this.toasterService.success("User updated successfully");
        this.router.navigate(['/users']);
      },
      (error) => {
        console.error("Error updating user", error);
        this.toasterService.error("Error updating user");
      }
    );
  }
}
