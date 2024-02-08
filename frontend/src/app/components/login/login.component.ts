import {Component, inject} from '@angular/core';
import {LoginService} from "../../services/login.service";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage: string | null = null;
  loginService = inject(LoginService)
  cookieService = inject(CookieService)
  router = inject(Router)
  onSubmit(form: NgForm){
    const email = form.value.email;
    const password = form.value.password;
    this.loginService.login(email, password).subscribe((loginResponse) => {
        this.cookieService.set('token', loginResponse.jwt);
        this.router.navigate(['/users']);
      }, error => {
        this.errorMessage = "Invalid credentials!";
      }
    )
    // clear the form
    form.reset();
  }
}
