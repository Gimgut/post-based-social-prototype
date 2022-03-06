import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../auth/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticatedOnlyService implements CanActivate {

  constructor(
    private router: Router,
    private authService: AuthenticationService
    ) { }

  canActivate(
    route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot)
    : boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
      
      if (this.authService.isAuthenticated) {
        return true;
      }
      this.router.navigate(["auth"]);
      return false;
  }
}
