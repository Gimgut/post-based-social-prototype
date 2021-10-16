import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { Post, PostAdapter } from '../models/post.model';
import { ApiRoutes } from './api.routes';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(
    private http: HttpClient,
    private postAdapter: PostAdapter,
    private apiRoutes: ApiRoutes
  ) {}

  getPost(id: string): Observable<Post> {
    return this.http.get(this.apiRoutes.getPost(id)).pipe(map(item => this.postAdapter.adapt(item)));
  }
}
