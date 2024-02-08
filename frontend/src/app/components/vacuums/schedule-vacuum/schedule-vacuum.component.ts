import {Component, inject} from '@angular/core';
import {VacuumsService} from "../../../services/vacuums.service";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {SearchOptions, Vacuum} from "../../../types/vacuum.types";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-schedule-vacuum',
  templateUrl: './schedule-vacuum.component.html',
  styleUrls: ['./schedule-vacuum.component.css']
})
export class ScheduleVacuumComponent {
  vacuumService = inject(VacuumsService);
  toaster = inject(ToastrService);
  router = inject(Router);
  vacuumName: Vacuum["name"] = "";
  statusOptions: SearchOptions[] = [
    {code: 0, name: 'Off'},
    {code: 1, name: 'On'},
    {code: 2, name: 'Discharge'},
  ]

  constructor() {
  }

  ngOnInit(): void {
    this.vacuumService.getVacuum(this.router.url.split("/")[2]).subscribe((vacuum) => {
      this.vacuumName = vacuum.name;
    })
  }

  onSubmit(form: NgForm) {
    this.vacuumService.scheduleVacuum(this.router.url.split("/")[2], form.value.date, form.value.status.name).subscribe((vacuum) => {
      this.router.navigate(['/vacuums']);
    }, (error) => {
      console.log(error);
      this.toaster.error(error.error.message, "Error");
    });
  }
}
