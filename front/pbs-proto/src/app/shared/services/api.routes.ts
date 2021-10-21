import { Injectable } from '@angular/core';
import { environment } from "../../../environments/environment"

@Injectable({
  providedIn: 'root'
})
export class ApiRoutes {

  apiUrl: string;

  constructor() {
    this.apiUrl = environment.urlApi;
  }

  getPost = (postId: string) =>
    `${this.apiUrl}/api/post/${postId}`;

  //Submit lastPostId = ''; to show first posts
  getFeedRecent = (lastPostId: string) =>
    `${this.apiUrl}/api/feed/recent/${lastPostId}`;

  createNewAccount = () =>
    `${this.apiUrl}/api/auth/signup`;

  loginWithEmailPassword = () =>
    `${this.apiUrl}/api/auth/signin`;

  loginWithGoogle = () =>
   `${this.apiUrl}/oauth2/authorization/google`;

  refreshToken = () =>
    `${this.apiUrl}/api/auth/refresh_token`;

  authExchangeEndpointForGoogle = () => 
    `${this.apiUrl}/login/oauth2/code/google`;
}
