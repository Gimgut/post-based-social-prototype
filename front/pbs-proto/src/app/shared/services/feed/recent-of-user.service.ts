import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RecentFeedStrategy } from '../../feed-strategy/recent.feed.strategy';
import { Post, PostAdapter } from '../../models/post.model';
import { ApiRoutes } from '../api.routes';

@Injectable({
  providedIn: 'root'
})
export class RecentFeedOfUserService extends RecentFeedStrategy {

  private userId: string = '';

  constructor(
    protected http: HttpClient,
    protected postAdapter: PostAdapter,
    protected apiRoutes: ApiRoutes
  ) {
    super(http, postAdapter);
    console.log('RecentService contructor');
  }

  setUserId(userId:string) {
    this.userId = userId;
  }

  fetchPosts(lastPostId: string): Observable<Post[]> {
    return this.http.get<any[]>(this.apiRoutes.getFeedRecentOfUser(this.userId,lastPostId)).pipe(
      map((data: any[]) => this.postAdapter.adaptArr(data)));
  }
}
