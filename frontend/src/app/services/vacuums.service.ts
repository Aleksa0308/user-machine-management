import {inject, Injectable} from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment.development";
import {Vacuum, VacuumsResponse} from "../types/vacuum.types";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class VacuumsService {
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

  getVacuums(): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.get<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/get`,
      { headers }
    );
  }

  getVacuum(id: string): Observable<Vacuum> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.get<Vacuum>(
      `${this.backendUrl}/api/vacuums/get/${id}`,
      { headers }
    );
  }

  getErrors(): Observable<Error[]>{
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.get<Error[]>(
      `${this.backendUrl}/api/vacuums/errors`,
      { headers }
    );
  }

  createVacuum(name: string): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();
    const body = {
      name
    };
    return this.httpClient.post<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/create`,
      body,
      { headers }
    );
  }

  destroyVacuum(id: string): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.delete<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/destroy/${id}`,
      { headers }
    );
  }

  startVacuum(id: string): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.get<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/start/${id}`,
      { headers }
    );
  }

  stopVacuum(id: string): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();
    return this.httpClient.get<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/stop/${id}`,
      { headers }
    );
  }

  dischargeVacuum(id: string): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();

    return this.httpClient.get<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/discharge/${id}`,
      { headers }
    );
  }

  searchVacuums(name: string, status: string, dateFrom: Date | "", dateTo: Date | ""): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();
    //get request with search params
    return this.httpClient.get<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/search?name=${name}&status=${status}&dateFrom=${dateFrom}&dateTo=${dateTo}`,
      { headers }
    );
  }

  scheduleVacuum(id: string, date: Date, action: string): Observable<VacuumsResponse> {
    const headers = this.getAuthorizationHeaders();
    const body = {
      id,
      date,
      action
    };
    return this.httpClient.post<VacuumsResponse>(
      `${this.backendUrl}/api/vacuums/schedule`,
      body,
      { headers }
    );
  }
}


