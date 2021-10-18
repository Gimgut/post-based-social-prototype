import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { RefreshTokenResponseAdapter, RefreshTokenResponseDto, RefreshTokenResponseStatus } from '../../dto/auth/refresh-token-response.dto';
import { User } from '../../models/user.model';
import { ApiRoutes } from '../api.routes';
import { ProfileService } from '../profile.service';

@Injectable({
  providedIn: 'root'
})
export class SecurityService implements OnInit {

  private readonly refreshTokenStorageKey = 'rt';

  isAuthenticating = false;
  refresh_token: string | null = null;
  access_token: string | null = null;
  authenticatedUser: User | null = null;

  constructor(
    private http: HttpClient,
    private apiRoutes: ApiRoutes,
    private refreshTokenResponseAdapter: RefreshTokenResponseAdapter
  ) { }

  ngOnInit(): void {
    console.log('Security service init');

    this.isAuthenticating = true;
    this.refresh_token = localStorage.getItem(this.refreshTokenStorageKey);

    if (this.refresh_token === null) {
      this.isAuthenticating = false;
      return;
    }

    this.callRefreshToken(this.refresh_token).subscribe(
      response => {
        const refreshTokenResponse: RefreshTokenResponseDto = this.refreshTokenResponseAdapter.adapt(response);
        if (refreshTokenResponse.status === RefreshTokenResponseStatus.SUCCESS) {
          this.refresh_token = refreshTokenResponse.refreshToken;
          this.access_token = refreshTokenResponse.accessToken;
          localStorage.setItem(this.refreshTokenStorageKey, refreshTokenResponse.refreshToken);
          this.authenticatedUser = refreshTokenResponse.userInfo;
        } else {
          this.resetAllInfo();
        }
        this.isAuthenticating = false;
      },
      (error: HttpErrorResponse) => {
        this.resetAllInfo();
        this.isAuthenticating = false;
      }
    )
  }

  public getAuthenticatedProfile() {
    return this.authenticatedUser;
  }

  public isAuthenticated() {
    return this.authenticatedUser === null;
  }

  private resetAllInfo() {
    this.refresh_token = null;
    this.access_token = null;
  }

  private callRefreshToken(refresh_token: String) {
    const requestBody = {
      refreshToken: refresh_token
    };
    return this.http.post(this.apiRoutes.signIn_Email(), requestBody, { observe: 'response' });
  }
}
