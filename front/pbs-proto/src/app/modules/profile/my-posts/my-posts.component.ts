import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/shared/models/post.model';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';
import { MyPostsRecentService } from './my-posts-recent.service';

@Component({
  selector: 'app-my-posts',
  templateUrl: './my-posts.component.html',
  styleUrls: ['./my-posts.component.scss']
})
export class MyPostsComponent implements OnInit {

  myPosts: Post[];

  constructor(
    private authSerivce: AuthenticationService,
    private recentService: MyPostsRecentService
  ) { 
    if (authSerivce.authenticatedUserValue) {
      recentService.setUserId(authSerivce.authenticatedUserValue.id.toString());
    }
    this.myPosts = recentService.getPosts();
    recentService.tryFetchMore();
  }

  ngOnInit(): void {
  }

  fetchMoreOnClick() {
    this.recentService.tryFetchMore();
  }

}
