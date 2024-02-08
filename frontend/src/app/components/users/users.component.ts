import {Component, inject} from '@angular/core';
import {UsersService} from "../../services/users.service";
import {ToastrService} from "ngx-toastr";
import {User} from "../../types/user.types";
import {MenuItem} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent {
    usersService = inject(UsersService);
    router = inject(Router);
    toaster = inject(ToastrService);

    users!: User[];
    canEdit = false;
    canDelete = false;
    canCreate = false;
    constructor() { }

    ngOnInit(): void {
      const permissions = this.usersService.checkUserPermissions();
      if(permissions.length === 0){
        this.toaster.warning("You don't have any permissions!", "Warning!");
      }else{
        this.getUsers();
        // go through permissions and check if user can edit or delete
        permissions.forEach((permission) => {
          if(permission === "can_update_users"){
            this.canEdit = true;
          }else if(permission === "can_delete_users"){
            this.canDelete = true;
          }else if(permission === "can_create_users"){
            this.canCreate = true;
          }
        })
      }
    }

    private getUsers(): void{
      this.usersService.getUsers().subscribe(
        (response) => {
          this.users = response.users;
          this.toaster.success("Data fetched successfully!");
        },
        (error) => {
          this.toaster.error(error, "Error");
        }
      )
    }

    navigateToCreateUser(): void{
      this.router.navigate(['/create-user']);
    }
    navigateToUpdateUser(id: string): void{
      this.router.navigate([`/update-user/${id}`]);
    }
  deleteUser(id: string): void{
      this.usersService.deleteUser(id).subscribe(
        (response) => {
          this.toaster.success("User deleted successfully!");
          // refresh users
          this.getUsers();
        },
        (error) => {
          this.toaster.error(error, "Error");
        }
      )
    }
}
