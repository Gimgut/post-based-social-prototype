import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { catchError, map } from 'rxjs/operators';
import { Post } from '../models/post.model';
import { User, UserAdapter } from '../models/user.model';
import { ApiRoutes } from './api.routes';
import { AuthenticationService } from './auth/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService implements OnInit {

  private subscriptions : User[] = [];
  private subscriptionsPosts: Post[] = [];

  constructor(
    private authService: AuthenticationService,
    private apiRoutes: ApiRoutes,
    private http: HttpClient,
    private userAdapter: UserAdapter
  ) { 
    console.log('SubscriptionService constructor');
    this.fetchSubscriptions()?.subscribe();
  }

  ngOnInit(): void {
  }

  public getSubscriptions() {
    return this.subscriptions;
  }

  public getSubscriptionPosts() {
    return this.subscriptionsPosts;
  }

  fetchSubscriptions() {
    if (!this.authService.isAuthenticated) {
      return;
    }
    return this.http.get(this.apiRoutes.getSubscriptions())
      .pipe(
        map(res => {
          const fetchedUsers = this.userAdapter.adaptArr(res);
          this.subscriptions.length = 0;
          this.subscriptions.push(...fetchedUsers);
          return fetchedUsers;
        })
    );
  }

  subscribe(idPublisher:number) {
    if (!this.authService.isAuthenticated) {
      return;
    }
    return this.http.post(this.apiRoutes.subscribe(idPublisher),"");
  }

  unsubscribe(idPublisher:number) {
    if (!this.authService.isAuthenticated) {
      return;
    }
    return this.http.post(this.apiRoutes.unsubscribe(idPublisher),"");
  }

  addSubscription(user: User) {
    this.subscriptions.push(user);
  }

  removeSubscription(user: User) {
    this.removeUserById(this.subscriptions, user.id);
  }

  private removeUserById(users: User[], id: number) {
    for( var i = 0; i < users.length; i++){ 
      if ( users[i].id === id) { 
          users.splice(i, 1);
          return; 
      }
    }
  }

}
