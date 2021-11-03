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
      title: title,
      content: text
    }
    return this.http.patch(this.apiRoutes.editPost(postId.toString()), requestBody);
  }

  deletePost(postId: number) {
    console.log("typeof postId = " +typeof(postId));
    return this.http.delete(this.apiRoutes.deletePost(postId.toString()));
  }
}
