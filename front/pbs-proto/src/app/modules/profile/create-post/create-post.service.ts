import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiRoutes } from 'src/app/shared/services/api.routes';

@Injectable({
  providedIn: 'root'
})
export class CreatePostService {

  constructor(
    private http : HttpClient,
    private apiRoutes : ApiRoutes
  ) { }

  //number = id of new post if succeess
  createPost(title: string, text: string): Observable<number> {
    const requestBody = {
      title: title,
      content: text
    }
    console.log('sending ' + requestBody.title + " " + requestBody.content);
    return this.http.post<number>(this.apiRoutes.createPost(), requestBody);
  }
}
