import { Injectable } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  user: User | undefined;

  constructor() {
    console.log('profile service constructor');
   }

  public setProfileUser(user: User) {
    this.user = user;
  }
}