import { Component, Input, OnInit } from '@angular/core';
import { Post } from 'src/app/shared/models/post.model';
import { RecentFeedOfUserService } from 'src/app/shared/services/feed/recent-of-user.service';

@Component({
  selector: 'app-user-feed-recent',
  templateUrl: './user-feed-recent.component.html',
  styleUrls: ['./user-feed-recent.component.scss']
})
export class UserFeedRecentComponent implements OnInit {

  @Input()
  posts:Post[] = [];

  constructor() { 
  }

  ngOnInit(): void {
  }

  fetchMoreOnClick() {
    //call subscribed method
    //this.recentService.tryFetchMore();
  }

}
