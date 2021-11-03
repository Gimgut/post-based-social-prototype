import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/shared/models/post.model';
import { ApiRoutes } from 'src/app/shared/services/api.routes';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';
import { RecentFeedOfUserService } from 'src/app/shared/services/feed/recent-of-user.service';
import { PostService } from 'src/app/shared/services/post.service';

@Component({
  selector: 'app-my-posts',
  templateUrl: './my-posts.component.html',
  styleUrls: ['./my-posts.component.scss']
})
export class MyPostsComponent implements OnInit {

  myPosts: Post[];

  constructor(
    private authSerivce: AuthenticationService,
    private recentService: RecentFeedOfUserService
  ) { 
    if (authSerivce.authenticatedUserValue) {
      recentService.setUserId(authSerivce.authenticatedUserValue.id.toString());
    }
    this.myPosts = recentService.getPosts();
    recentService.tryFetchMore();
  }

  ngOnInit(): void {
    if (!this.authSerivce.isAuthenticated) {
      return;
    }
  }

  fetchMoreOnClick() {
    this.recentService.tryFetchMore();
  }

}
