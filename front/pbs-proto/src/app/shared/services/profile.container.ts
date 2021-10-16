import { Injectable } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileContainer {

  user: User | undefined;

  constructor() {
    console.log('profile container constructor');
   }

  public setProfileUser(user: User) {
    this.user = user;
  }
}
