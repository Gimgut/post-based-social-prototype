import { Injectable } from '@angular/core';
import { environment } from "../../../environments/environment"

@Injectable({
  providedIn: 'root'
})
export class ApiRoutes {

  apiUrl: string;
  apiVersion: string;
  apiFullPath : string;

  constructor() {
    this.apiUrl = environment.urlApi;
    this.apiVersion = environment.apiVersion;
    this.apiFullPath = `${this.apiUrl}/api/${this.apiVersion}`
  }


  //#region POST CRUD
  createPost = () =>
    `${this.apiFullPath}/post`;

  getPost = (postId: string) =>
    `${this.apiFullPath}/post/${postId}`;

  editPost = (postId: string) =>
    `${this.apiFullPath}/post/${postId}`;

  deletePost = (postId: string) =>
    `${this.apiFullPath}/post/${postId}`;
  //#endregion


  //#region SUBSCRIPTION
  subscribe = (idPublisher: number) =>
    `${this.apiFullPath}/subscription/subscribe/${idPublisher}`

  unsubscribe = (idPublisher: number) =>
    `${this.apiFullPath}/subscription/unsubscribe/${idPublisher}`

  getSubscriptions = () =>
    `${this.apiFullPath}/subscription`;
  //#endregion


  //#region USER
  getUserInfoById = (userId: string) => 
    `${this.apiFullPath}/user/id/${userId}`;
  //#endregion


  //#region FEED
  /**
   * Submit lastPostId = '' to show first posts
   * @param lastPostId 
   * @returns 
   */
  getFeedRecent = (lastPostId: string) =>
    `${this.apiFullPath}/feed/recent?lastPostId=${lastPostId}`;

  getFeedRecentOfUser = (userId: string,lastPostId: string) =>
    `${this.apiFullPath}/feed/recent/user/${userId}?lastPostId=${lastPostId}`;

  getSubscriptionsFeedRecent = (lastPostId: string) =>
    `${this.apiFullPath}/feed/subscriptions/recent?lastPostId=${lastPostId}`;
  //#endregion


  //#region AUTH
  createNewAccount = () =>
    `${this.apiFullPath}/auth/signup`;

  loginWithEmailPassword = () =>
    `${this.apiFullPath}/auth/signin`;

  loginWithGoogle = () =>
   `${this.apiUrl}/api/oauth2/authorization/google`;

  refreshToken = () =>
    `${this.apiFullPath}/auth/refresh_token`;

  authExchangeEndpointForGoogle = () => 
    `${this.apiUrl}/api/oauth2/code/google`;
  //#endregion
}
