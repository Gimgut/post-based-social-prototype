import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/shared/models/post.model';
import { User } from 'src/app/shared/models/user.model';
import { SubscriptionFeedService } from 'src/app/shared/services/subscription-feed.service';
import { SubscriptionService } from 'src/app/shared/services/subscription.service';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.scss']
})
export class SubscriptionsComponent implements OnInit {


  public subscriptions : User[] = [];
  public recentPosts: Post[];

  constructor(
    private subscriptionService: SubscriptionService,
    private subscriptionFeedService: SubscriptionFeedService
  ) { 
    /*
      if (this.subscriptionService.subscriptionsChanged === true) {
       this.subscriptionFeedService.reloadFeed();
       this.subscriptionService.subscriptionsChanged = false;
       }
     */  
    this.recentPosts = subscriptionFeedService.getRecentPosts();
    this.subscriptions = this.subscriptionService.getSubscriptions();
  }

  ngOnInit(): void {
  }

  fetchMore() {
    this.subscriptionFeedService.tryFetchMore();
  }

}
