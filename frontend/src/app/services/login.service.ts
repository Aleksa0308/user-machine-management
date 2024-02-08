import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.development";
import {BehaviorSubject, Observable} from "rxjs";
import {LoginResponse} from "../types/user.types";
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  httpClient = inject(HttpClient);
  cookieService = inject(CookieService);

  private _authStatus = new BehaviorSubject<boolean>(false);
  authStatus = this._authStatus.asObservable();

  private readonly backendUrl = environment.backend;
  constructor() {
    this._authStatus.next(this.checkIfHasSavedToken());
  }

  login(email: string, password: string): Observable<LoginResponse>{
    return this.httpClient.post<LoginResponse>(`${this.backendUrl}/auth/login`, {
      email: email,
      password: password
    });
  }

  logout(): void {
    this.cookieService.delete('token');
    this._authStatus.next(false);
  }

  checkIfHasSavedToken(): boolean{
    return this.cookieService.check('token');
  }
}
