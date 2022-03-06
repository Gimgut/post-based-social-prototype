import { Component, Input, OnInit } from '@angular/core';
import { Post } from 'src/app/shared/models/post.model';

@Component({
  selector: 'app-user-feed-recent',
  templateUrl: './user-feed-recent.component.html',
  styleUrls: ['./user-feed-recent.component.scss']
})
export class UserFeedRecentComponent implements OnInit {

  @Input()
  posts:Post[] = [];

  constructor() { 
    console.log('constructor user feed recent');
  }

  ngOnInit(): void {
    console.log('init constructor user feed recent');
  }

  fetchMoreOnClick() {
    //call subscribed method
    //this.recentService.tryFetchMore();
  }

}
