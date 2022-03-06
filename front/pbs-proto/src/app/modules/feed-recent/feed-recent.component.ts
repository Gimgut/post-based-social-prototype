import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/shared/models/post.model';
import { FeedRecentService } from './feed-recent.service';

@Component({
  selector: 'app-feed-recent',
  templateUrl: './feed-recent.component.html',
  styleUrls: ['./feed-recent.component.scss']
})
export class FeedRecentComponent implements OnInit {

  posts: Post[] = [];

  constructor(
    private feedRecentService: FeedRecentService
  ) {
  }

  ngOnInit(): void {
    //this.feedRecentService.getRecent('').subscribe((x) => this.posts = x);
    this.posts = this.feedRecentService.getPosts();
  }

  fetchMore() {
    this.feedRecentService.tryFetchMore();
  }

}
