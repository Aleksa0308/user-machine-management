import { inject,Injectable } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {jwtDecode} from "jwt-decode";
import {decodedJwt, Permission, UserResponse, UsersResponse} from "../types/user.types";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment.development";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  cookieService= inject(CookieService);
  httpClient = inject(HttpClient);

  private readonly backendUrl = environment.backend;
  constructor() { }

  private getAuthorizationHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    if (token) {
      const headers = new HttpHeaders({
        Authorization: `Bearer ${token}`,
      });
      return headers;
    }
    return new HttpHeaders();
  }

  checkUserPermissions(): String[]{
    const token = this.cookieService.get('token');
    if(token){
      try {
        const decodedToken: decodedJwt = jwtDecode(token);
        return decodedToken.permissions;
      }catch (e) {
        return [];
      }
    }
    return [];
  }

  getUsers(): Observable<UsersResponse> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.get<UsersResponse>(
      `${this.backendUrl}/api/users/get`,
      { headers }
    );
  }

  createUser(firstname: string, lastname: string, email: string, password: string, permissions: String[]): Observable<UsersResponse> {
    const headers = this.getAuthorizationHeaders();
    const body = {
      firstname,
      lastname,
      email,
      password,
      permissions: permissions
    };
    return this.httpClient.post<UsersResponse>(
      `${this.backendUrl}/api/users/register`,
      body,
      { headers }
    );
  }

  updateUser(userId: string, firstname: string, lastname: string, email: string, permissions: String[]): Observable<UsersResponse> {
    const headers = this.getAuthorizationHeaders();
    const body = {
      userId,
      firstname,
      lastname,
      email,
      permissions: permissions
    };
    return this.httpClient.put<UsersResponse>(
      `${this.backendUrl}/api/users/update/${userId}`,
      body,
      { headers }
    );
  }

  getUserById(id: string): Observable<UserResponse> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.get<UserResponse>(
      `${this.backendUrl}/api/users/get/${id}`,
      { headers }
    );
  }

  deleteUser(id: string): Observable<UsersResponse> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.delete<UsersResponse>(
      `${this.backendUrl}/api/users/delete/${id}`,
      { headers }
    );
  }

}
