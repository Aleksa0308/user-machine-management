import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {CookieService} from "ngx-cookie-service";
import {decodedJwt} from "../types/user.types";
import {jwtDecode} from "jwt-decode";

export const scheduleVacuumGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const cookieService = inject(CookieService);

  const token = cookieService.get('token');

  if(token){
    try {
      const decodedToken: decodedJwt = jwtDecode(token);
      const permissions = decodedToken.permissions;
      if(permissions.includes("can_schedule_vacuums")){
        return true;
      }
    }catch (e) {
      router.navigate(['/']);
      return false;
    }
    router.navigate(['/']);
    return false;
  }
  router.navigate(['/']);
  return false;
};
