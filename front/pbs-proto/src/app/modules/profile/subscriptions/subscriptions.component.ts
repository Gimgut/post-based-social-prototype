import { Component, OnInit } from '@angular/core';
import { pipe } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from 'src/app/shared/models/user.model';
import { SubscriptionService } from 'src/app/shared/services/subscription.service';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.scss']
})
export class SubscriptionsComponent implements OnInit {


  public subscriptions : User[] = [];

  constructor(
    private subscriptionService: SubscriptionService
  ) { 
  }

  ngOnInit(): void {
    console.log('SubscriptionsComponent oninit')
    this.subscriptions = this.subscriptionService.subscriptions;
  }

}
