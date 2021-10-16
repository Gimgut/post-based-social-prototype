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
    `${this.apiUrl}/api/post/${postId}`

  //Submit lastPostId = ''; to show first posts
  getFeedRecent = (lastPostId: string) =>
    `${this.apiUrl}/api/feed/recent/${lastPostId}`

  createNewAccount = () =>
    `${this.apiUrl}/api/auth/signup`

  signIn_Email = () =>
    `${this.apiUrl}/api/auth/signin`
}
