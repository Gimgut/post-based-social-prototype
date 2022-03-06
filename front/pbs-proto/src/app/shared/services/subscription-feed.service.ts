import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Post, PostAdapter } from '../models/post.model';
import { ApiRoutes } from './api.routes';
import { AuthenticationService } from './auth/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionFeedService implements OnInit {

  private recentPosts: Post[] = []
  private isFetching: boolean = false;
  private noMorePosts: boolean = false;

  constructor(
    private authService: AuthenticationService,
    private http: HttpClient,
    private apiRoutes: ApiRoutes,
    private postAdapter: PostAdapter
  ) {
    console.log('SubscriptionFeedService constructor')
    this.tryFetchMore();
   }

  ngOnInit(): void {
  }

  private addRecentPostsAfterFetching(posts: Post[]) {
    this.isFetching = false;
    this.recentPosts.push(...posts);
    if (posts.length < 1) {
      this.noMorePosts = true;
    } else {
      this.noMorePosts = false;
    }
  }

  private fetchRecentPosts(lastPostId: string) : Observable<Post[]> {
    this.isFetching = true;
    return this.http.get<any[]>(this.apiRoutes.getSubscriptionsFeedRecent(lastPostId))
      .pipe(
        map((data: any[]) => this.postAdapter.adaptArr(data))
      );
  }

  private fetchMoreRecentPosts(): void {
    const lastRecentPostId = 
      this.recentPosts.length > 0
        ? this.recentPosts[this.recentPosts.length-1].id.toString()
        : '';
    this.fetchRecentPosts(lastRecentPostId).subscribe((x) => this.addRecentPostsAfterFetching(x));
  }

  getRecentPosts(): Post[] {
    return this.recentPosts;
  }

  tryFetchMore(): void {
    if (!this.authService.isAuthenticated
      || this.isFetching) {
      return;
    }
    this.fetchMoreRecentPosts();
  }


}


