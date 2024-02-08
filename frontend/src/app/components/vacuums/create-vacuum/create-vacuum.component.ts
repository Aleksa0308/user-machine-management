import {Component, inject} from '@angular/core';
import {VacuumsService} from "../../../services/vacuums.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgForm} from "@angular/forms";
import {Permission} from "../../../types/user.types";

@Component({
  selector: 'app-create-vacuum',
  templateUrl: './create-vacuum.component.html',
  styleUrls: ['./create-vacuum.component.css']
})
export class CreateVacuumComponent {
  vacuumService = inject(VacuumsService);
  router = inject(Router);
  toasterService = inject(ToastrService);

  constructor() {
  }

  onSubmit(form: NgForm){
    const name = form.value.name;
    // call the service
    this.vacuumService.createVacuum(name).subscribe((usersResponse) => {
      this.router.navigate(['/vacuums']);
      //toast success
      this.toasterService.success("Vacuum created successfully!");
    }, error => {
      //toast error
      this.toasterService.error("Error while creating a vacuum!");
    });
    // clear the form
    form.reset();
  }
}
