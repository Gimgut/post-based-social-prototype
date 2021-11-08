import { Component, Input, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user.model';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';
import { SubscriptionService } from 'src/app/shared/services/subscription.service';

@Component({
  selector: 'app-sub-buttons',
  templateUrl: './sub-buttons.component.html',
  styleUrls: ['./sub-buttons.component.scss']
})
export class SubButtonsComponent implements OnInit {
  
  private _author:User|undefined = undefined;
  isSubscribed: boolean|undefined = undefined;
  httpPending = false;
  isAuthorized = false;

  constructor(
    private subscriptionService: SubscriptionService,
    private authService: AuthenticationService
  ) { 
    this.isAuthorized = authService.isAuthenticated;
  }

  ngOnInit(): void {
  }

  @Input()
  set author(userInfo: User|undefined) {
    this._author = userInfo;
    if (userInfo) {
      this.refreshSubscribedStatus();
    }
  }

  get author() : User|undefined {
    return this._author;
  }

  setAuthor(authorUserInfo: User) {
    this.author = authorUserInfo;
    this.refreshSubscribedStatus();
  }

  isOwner() {
    if (this.author?.id === this.authService.authenticatedUserValue?.id
      && this.author && this.authService.authenticatedUserValue) {
      return true;
    }
    return false;
  }

  refreshSubscribedStatus() {
    if (!this.author) {
      return;
    }
    this.isSubscribed = this.subscriptionService.checkIsSubscribed(this.author.id);
  }

  subscribe() {
    if (!this.author || this.httpPending) {
      return;
    }
    this.httpPending = true;
    this.subscriptionService.subscribe(this.author)?.subscribe(
      data => {
        this.isSubscribed=true;
        this.refreshSubscribedStatus();
        this.httpPending = false;
      },
      error => {
        this.httpPending = false;
      }
    );
  }

  unsubscribe() {
    if (!this.author || this.httpPending) {
      return;
    }
    this.httpPending = true;
    this.subscriptionService.unsubscribe(this.author)?.subscribe(
      data => {
        this.isSubscribed=false;
        this.refreshSubscribedStatus();
        this.httpPending = false;
      },
      error => {
        this.httpPending = false;
      }
    );
  }

}
