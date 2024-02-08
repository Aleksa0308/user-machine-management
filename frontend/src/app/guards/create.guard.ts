import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {CookieService} from "ngx-cookie-service";
import {jwtDecode} from "jwt-decode";
import {decodedJwt} from "../types/user.types";

export const createGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const cookieService = inject(CookieService);

  const token = cookieService.get('token');

  if(token){
    try {
      const decodedToken: decodedJwt = jwtDecode(token);
      const permissions = decodedToken.permissions;
      if(permissions.includes("can_create_users")){
        return true;
      }
    }catch (e) {
      router.navigate(['/users']);
      return false;
    }
    router.navigate(['/users']);
    return false;
  }
  router.navigate(['/']);
  return false;
};
