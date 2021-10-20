import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LoginResponseDto, LoginResponseStatus } from '../../dto/auth/login-response.dto';
import { RefreshTokenResponseDto, RefreshTokenResponseStatus } from '../../dto/auth/refresh-token-response.dto';
import { User } from '../../models/user.model';
import { ApiRoutes } from '../api.routes';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private readonly refreshTokenStorageKey: string = 'rt';
  private userSubject: BehaviorSubject<User|null>;
  public accessToken: string|null = null;
  public refreshToken: string|null = null;
  private refreshTokenTimeoutId:any = null;

  constructor(
    private router: Router,
    private http: HttpClient,
    private apiRoutes: ApiRoutes
  ) { 
    this.userSubject = new BehaviorSubject<User|null>(null);
    console.log('AuthenticationService constructor')
  }

  public get authenticatedUserValue() : User | null {
    return this.userSubject.getValue();
  }

  public getRtStorageKey(): string {
    return this.refreshTokenStorageKey.toString();
  }

  private applyRefreshToken(refreshToken: string) {
    this.refreshToken = refreshToken;
    localStorage.setItem(this.getRtStorageKey(), refreshToken);
    this.startRefreshTokenEventTimer(refreshToken);
  }

  private applyAccessToken(accessToken: string) {
    this.accessToken = accessToken;
  }

  private authenticate(userInfo: User, accessToken:string, refreshToken:string) {
    this.userSubject.next(userInfo);
    this.applyAccessToken(accessToken);
    this.applyRefreshToken(refreshToken);
  }

  loginWithEmailPassword(email: string, password: string) : Observable<LoginResponseDto> {
    const requestBody = {
      email: email,
      password: password
    };

    return this.http.post<LoginResponseDto>(this.apiRoutes.loginWithEmailPassword(), requestBody)
      .pipe(map(
        res => {
          if (res.status === LoginResponseStatus.SUCCESS) {
            this.authenticate(res.userInfo, res.accessToken, res.refreshToken);
          }
          return res;
        }));
  }

  loginWithGoogle() {
    window.location.href=this.apiRoutes.loginWithGoogle();
  }

  logout() {
    localStorage.removeItem(this.getRtStorageKey());
    this.refreshToken = null;
    this.accessToken = null;
    this.userSubject.next(null);
    this.router.navigate(['/recent']);
  }

  callRefreshToken(refreshToken: string) : Observable<RefreshTokenResponseDto> {
    return this.http.post<RefreshTokenResponseDto>(this.apiRoutes.refreshToken(), refreshToken)
      .pipe(map(
        res => {
          if (res.status === RefreshTokenResponseStatus.SUCCESS) {
            console.log('callRefreshToken success')
            this.authenticate(res.userInfo, res.accessToken, res.refreshToken);
          }
          return res;
        }));
  }
  
  startRefreshTokenEventTimer(refreshToken: string) {
    if (this.refreshTokenTimeoutId!==null)
      return;

    const payload = JSON.parse(atob(refreshToken.split('.')[1]));
    const timeout : number = Number(payload.aexp) - 1000*60;
    this.refreshTokenTimeoutId = setTimeout(() => {
      this.refreshTokenTimeoutId = null;
      this.callRefreshToken(refreshToken).subscribe();
    }, timeout);
  }

  stopRefreshTokenEventTimer() {
    clearTimeout(this.refreshTokenTimeoutId);
    this.refreshTokenTimeoutId = null;
  }

}
