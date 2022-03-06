import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post, PostAdapter } from '../models/post.model';

@Injectable({
  providedIn: 'root'
})
export abstract class RecentFeedStrategy {
    private posts: Post[] = []
    private isFetching: boolean = false;
    noMorePosts: boolean = false;
  
    constructor(
      protected http: HttpClient,
      protected postAdapter: PostAdapter
    ) { 
        console.log('RecentFeedStrategy contructor');
    }
  
    private addPostsAfterFetching(posts: Post[]) {
      this.isFetching = false;
      this.posts.push(...posts);
      if (posts.length < 1) {
        this.noMorePosts = true;
      } else {
        this.noMorePosts = false;
      }
    }
  
    abstract fetchPosts(lastPostId: string): Observable<Post[]>;
  
    private fetchFirstPosts(): void {
        this.isFetching = true;
        this.fetchPosts('').subscribe((x) => this.addPostsAfterFetching(x));
    }
  
    private fetchMorePosts(): void {
        let lastPostId = (this.posts[this.posts.length - 1].id).toString();
        this.isFetching = true;
        this.fetchPosts(lastPostId).subscribe((x) => this.addPostsAfterFetching(x));
    }
  
    getPosts(): Post[] {
      return this.posts;
    }
  
    tryFetchMore(): void {
      if (this.isFetching) {
        return;
      }
      if (this.posts.length < 1) {
        this.fetchFirstPosts();
      } else {
        this.fetchMorePosts();
      }
    }

    removeAll() : void {
      this.posts.length=0;
    }
}