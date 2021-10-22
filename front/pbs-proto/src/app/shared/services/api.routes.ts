import { Injectable } from '@angular/core';
import { environment } from "../../../environments/environment"

@Injectable({
  providedIn: 'root'
})
export class ApiRoutes {

  apiUrl: string;
  apiVersion: string;

  constructor() {
    this.apiUrl = environment.urlApi;
    this.apiVersion = environment.apiVersion;
  }

  getPost = (postId: string) =>
    `${this.apiUrl}/api/${this.apiVersion}/post/${postId}`;

  //Submit lastPostId = ''; to show first posts
  getFeedRecent = (lastPostId: string) =>
    `${this.apiUrl}/api/${this.apiVersion}/feed/recent?lastPostId=${lastPostId}`;

  createNewAccount = () =>
    `${this.apiUrl}/api/${this.apiVersion}/auth/signup`;

  loginWithEmailPassword = () =>
    `${this.apiUrl}/api/${this.apiVersion}/auth/signin`;

  loginWithGoogle = () =>
   `${this.apiUrl}/oauth2/authorization/google`;

  refreshToken = () =>
    `${this.apiUrl}/api/${this.apiVersion}/auth/refresh_token`;

  authExchangeEndpointForGoogle = () => 
    `${this.apiUrl}/login/oauth2/code/google`;
}
