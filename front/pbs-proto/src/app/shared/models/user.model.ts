import { Injectable } from "@angular/core";
import { Adapter } from "./adapter.model";

export enum Roles {
  READER = 'READER',
  WRITER = 'WRITER',
  ADMIN = 'ADMIN'
}

export class User {
  constructor(
    public id: number,
    public username: string,
    public picture: string,
    public role: Roles,
    public registrationTime: Date
  ) { 
  }
}


@Injectable({
  providedIn: "root",
})
export class UserAdapter implements Adapter<User> {

  constructor(
  ) {}

  adapt(item: any): User {
    return new User(item.id, item.username, item.pictureUrl, <Roles>item.role, item.registrationTime);
  }

  adaptArr(arr: any): User[] {
    return arr.map((item: any) => this.adapt(item));
  }
}

