import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiRoutes } from 'src/app/shared/services/api.routes';

@Injectable({
  providedIn: 'root'
})
export class PostEditService {

  constructor(
    private http : HttpClient,
    private apiRoutes : ApiRoutes
  ) { }

  editPost(postId: number, title: string, text: string) {
    const requestBody = {
      postId: postId,
      title: title,
      content: text
    }
    return this.http.post(this.apiRoutes.editPost(), requestBody);
  }

  deletePost(postId: number) {
    console.log("typeof postId = " +typeof(postId));
    return this.http.delete(this.apiRoutes.deletePost(postId.toString()));
  }
}
