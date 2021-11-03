import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User, UserAdapter } from '../models/user.model';
import { ApiRoutes } from './api.routes';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private userAdapter: UserAdapter,
    private apiRoutes: ApiRoutes
  ) { }

  getUserInfo(userId: string): Observable<User> {
    return this.http.get(this.apiRoutes.getUserInfoById(userId)).pipe(map(item => this.userAdapter.adapt(item)));
  }
}
