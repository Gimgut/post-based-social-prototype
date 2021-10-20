import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { LoginResponseDto, LoginResponseStatus } from '../../dto/auth/login-response.dto';
import { User } from '../../models/user.model';
import { ApiRoutes } from '../api.routes';
import { ProfileService } from '../profile.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationServiceOld implements OnInit {

  user: User | undefined;
  isAuthenticated = false;
  isAuthenticating = false;

  isAuthenticationSuccess = false;
  isAuthenticationFailed = false;

  access_token: string | null = null;
  refresh_token: string | null = null;

  successfulAuthenticationEvent = new Subject();
  failedAuthenticationEvent = new Subject();

  i = 1;

  constructor(
    private http: HttpClient,
    private apiRoutes: ApiRoutes,
    private profileService: ProfileService
    ) { }
  
  ngOnInit(): void {
    this.refresh_token = localStorage.getItem('refresh_token');

    //try authorization by refresh token if possible
    if (this.refresh_token != null && this.refresh_token.length > 0) {
      //this.tryRefreshToken();
    }
  }

  /*
  tryRefreshToken() {
    this.http
  }
  */

  /*
  tryGoogleAuth() {
    //window.location.href=this.apiRoutes.apiUrl + "/login/oauth2/code/google";
    window.location.href=this.apiRoutes.apiUrl +"/oauth2/authorization/google";
  }
*/
  /*
  trySignIn_ByEmail(email: string, password: string) {

    if (this.isAuthenticating || this.isAuthenticated)
      return;

    this.isAuthenticating = true;

    const requestBody = {
      email: email,
      password: password
    };

    this.http.post(this.apiRoutes.loginWithEmailPassword(), requestBody, { observe: 'response' }).subscribe(
      response => {
        const loginResponse: LoginResponseDto = this.loginResponseAdapter.adapt(response.body);
        console.log("status: " + loginResponse.status);
        console.log("userdto: " + loginResponse.userInfo);
        switch (loginResponse.status) {

          case LoginResponseStatus.SUCCESS:
            this.isAuthenticated = true;
            this.access_token = response.headers.get('access_token') ?? '';
            this.refresh_token = response.headers.get('refresh_token') ?? '';
            localStorage.setItem('refresh_token', this.refresh_token);
            this.user = loginResponse.userInfo;
            this.isAuthenticationSuccess = true;
            this.isAuthenticationFailed = false;

            this.profileService.setProfileUser(this.user);

            console.log("id: " + this.user.id);
            console.log("username: " + this.user.username);
            this.successfulAuthenticationEvent.next(this.i++);
            break;

          case LoginResponseStatus.FAILED:
            this.isAuthenticationFailed = true;
            this.isAuthenticationSuccess = false;
            this.failedAuthenticationEvent.next(this.i++);
            break;
        }
        this.isAuthenticating = false;
      },
      (error: HttpErrorResponse) => {
        console.error('Authentication error!', error);
        this.isAuthenticationFailed = true;
        this.isAuthenticationSuccess = false;
        this.isAuthenticating = false;
        this.failedAuthenticationEvent.next(this.i++);
      }
    )
  }
*/
  logOut() {
    this.user = undefined;
    this.isAuthenticated = false;

    this.isAuthenticationSuccess = false;
    this.isAuthenticationFailed = false;

    this.access_token = null;
    this.refresh_token = null;;

    this.profileService.user = undefined;
  }
}

