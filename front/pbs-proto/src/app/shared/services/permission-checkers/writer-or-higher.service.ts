import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Roles } from '../../models/user.model';
import { AuthenticationService } from '../auth/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class WriterOrHigherService implements CanActivate {

  constructor(
    private router: Router,
    private authService: AuthenticationService
    ) { }

    canActivate(
      route: ActivatedRouteSnapshot, 
      state: RouterStateSnapshot)
      : boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        
        if (this.authService.isAuthenticated) {
          if (this.authService.authenticatedUserValue?.role === Roles.WRITER
            || this.authService.authenticatedUserValue?.role === Roles.ADMIN) {
              return true;
            }
        }
        this.router.navigate([""]);
        return false;
    }
}
