import {Component, inject} from '@angular/core';
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {VacuumsService} from "../../services/vacuums.service";
import {SearchOptions, SearchVacuums, Vacuum} from "../../types/vacuum.types";
import {UsersService} from "../../services/users.service";
import {NgForm} from "@angular/forms";
import {MenuItem, MenuItemCommandEvent} from "primeng/api";

@Component({
  selector: 'app-vacuums',
  templateUrl: './vacuums.component.html',
  styleUrls: ['./vacuums.component.css']
})
export class VacuumsComponent {

  vacuumsService = inject(VacuumsService);
  usersService = inject(UsersService);
  router = inject(Router);
  toaster = inject(ToastrService);

  search!: SearchVacuums;
  statusOptions: SearchOptions[] = [
    {code: 0, name: 'Off'},
    {code: 1, name: 'On'},
    {code: 2, name: 'Discharging'},
  ]
  vacuums!: Vacuum[];
  canDelete = false;
  canCreate = false;
  canStart = false;
  canStop = false;
  canSchedule = false;
  canDischarge = false;
  constructor() {
    this.search = {
          name: "",
          status: "",
          dateFrom: "",
          dateTo: ""
        }
  }

  ngOnInit(): void {
    const permissions = this.usersService.checkUserPermissions();
    if(permissions.length === 0){
      this.toaster.warning("You don't have any permissions!", "Warning!");
    }else{
      this.getVacuums();
      // go through permissions and check if user can edit or delete
      permissions.forEach((permission) => {
        if(permission === "can_destroy_vacuums"){
          this.canDelete = true;
        }else if(permission === "can_create_vacuums"){
          this.canCreate = true;
        }else if(permission === "can_start_vacuums"){
          this.canStart = true;
        }else if(permission === "can_stop_vacuums"){
          this.canStop = true;
        }else if(permission === "can_schedule_vacuums"){
          this.canSchedule = true;
        }else if(permission === "can_discharge_vacuums"){
          this.canDischarge = true;
        }
      })
    }
  }

  onSubmit(form: NgForm){
    const { name, dateFrom, dateTo } = form.value;
    const statuses: SearchOptions[] = form.value.status;
    let selectedStatuses = [];
    let statusesString = "";
    if(statuses.length > 0){
      selectedStatuses = statuses.map((status: SearchOptions) => status.code);
      statusesString = selectedStatuses.join(",");
    }
    // call the service
    this.vacuumsService.searchVacuums(name, statusesString, dateFrom, dateTo).subscribe((vacuumsResponse) => {
      this.vacuums = vacuumsResponse.vacuums;
      //toast success
      this.toaster.success("Vacuums fetched successfully!");
    }, error => {
      //toast error
      this.toaster.error("Error while fetching vacuums!");
    });
    console.log(form.value);
  }

  private getVacuums(): void{
    this.vacuumsService.getVacuums().subscribe(
      (response) => {
        this.vacuums = response.vacuums;
        this.toaster.success("Data fetched successfully!");
      },
      (error) => {
        this.toaster.error(error, "Error");
      }
    )
  }

  navigateToCreateVacuum(): void{
    this.router.navigate(['/create-vacuum']);
  }

  navigateToScheduleVacuum(id: string): void{
    this.router.navigate(['/schedule-vacuum', id]);
  }

  startVacuum(id: string): void{
    this.vacuumsService.startVacuum(id).subscribe(
      (response) => {
        this.toaster.success("Vacuum started successfully!");
        // refresh users
        this.getVacuums();
      },
      (error) => {
        this.toaster.error("", "Error");
      }
    )
  }

  stopVacuum(id: string): void{
    this.vacuumsService.stopVacuum(id).subscribe(
      (response) => {
        this.toaster.success("Vacuum stopped successfully!");
        // refresh users
        this.getVacuums();
      },
      (error) => {
        this.toaster.error("", "Error");
      }
    )
  }

  dischargeVacuum(id: string): void{
    this.vacuumsService.dischargeVacuum(id).subscribe(
      (response) => {
        this.toaster.success("Vacuum discharged successfully!");
        // refresh users
        this.getVacuums();
      },
      (error) => {
        this.toaster.error("", "Error");
      }
    )
  }
  destroyVacuum(id: string): void{
    this.vacuumsService.destroyVacuum(id).subscribe(
      (response) => {
        this.toaster.success("Vacuum deleted successfully!");
        // refresh users
        this.getVacuums();
      },
      (error) => {
        this.toaster.error(error, "Error");
      }
    )
  }

}

