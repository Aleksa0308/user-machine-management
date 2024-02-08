import {Component, inject} from '@angular/core';
import {VacuumsService} from "../services/vacuums.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-errors',
  templateUrl: './errors.component.html',
  styleUrls: ['./errors.component.css']
})
export class ErrorsComponent {
  vacuumService = inject(VacuumsService);
  toaster = inject(ToastrService);
  errors!: Error[]
  constructor() { }

  ngOnInit(): void {
    this.getErrors();
  }

  getErrors(){
    this.vacuumService.getErrors().subscribe((errors) => {
      this.errors = errors;
      this.toaster.success("Errors loaded successfully!", "Success!");
    }, (error) => {
      this.toaster.error(error.error.message, "Error!");
    })
  }
}
