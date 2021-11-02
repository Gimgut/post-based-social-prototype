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

  //TODO: Change array to dictionary/add dictionary
  checkIsSubscribed(idPublisher: number) : boolean {
    for (var u of this.subscriptions) {
      if (u.id === idPublisher) {
        return true;
      }
    }
    return false;
  }

  subscribe(publisher: User) {
    if (!this.authService.isAuthenticated) {
      return;
    }
    return this.http.post(this.apiRoutes.subscribe(publisher.id),"").pipe(
      map(res => {
        this.addSubscription(publisher);
      })
    );
  }

  unsubscribe(publisher:User) {
    if (!this.authService.isAuthenticated) {
      return;
    }
    return this.http.post(this.apiRoutes.unsubscribe(publisher.id),"").pipe(
      map(res => {
        this.removeSubscription(publisher);
      })
    );
  }

  private addSubscription(user: User) {
    this.subscriptions.push(user);
  }

  private removeSubscription(user: User) {
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
