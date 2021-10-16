import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Post, PostAdapter } from 'src/app/shared/models/post.model';
import { ApiRoutes } from 'src/app/shared/services/api.routes';

@Injectable({
  providedIn: 'root'
})
export class FeedRecentService {

  private posts: Post[] = []
  private isFetching: boolean = false;
  noMorePosts: boolean = false;

  constructor(
    private http: HttpClient,
    private postAdapter: PostAdapter,
    private apiRoutes: ApiRoutes
  ) { }

  private addPostsAfterFetching(posts: Post[]) {
    this.isFetching = false;
    this.posts.push(...posts);
    if (posts.length < 1)
      this.noMorePosts = true;
    else
      this.noMorePosts = false;
  }

  private fetchPosts(lastPostId: string): Observable<Post[]> {
    this.isFetching = true;
    return this.http.get<any[]>(this.apiRoutes.getFeedRecent(lastPostId)).pipe(
      map((data: any[]) => this.postAdapter.adaptArr(data)));
  }

  private fetchFirstPosts(): void {
    this.fetchPosts('').subscribe((x) => this.addPostsAfterFetching(x));
  }

  private fetchMorePosts(): void {
    let lastPostId = (this.posts[this.posts.length - 1].id).toString();
    this.fetchPosts(lastPostId).subscribe((x) => this.addPostsAfterFetching(x));
  }

  getPosts(): Post[] {
    if (this.posts.length < 1)
      this.fetchFirstPosts();
    return this.posts;
  }

  tryFetchMore(): void {
    if (this.isFetching)
      return;
    if (this.posts.length < 1)
      this.fetchFirstPosts();
    this.fetchMorePosts();
  }
}
