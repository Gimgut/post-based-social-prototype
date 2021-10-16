import { Injectable } from "@angular/core";
import { Adapter } from "./adapter.model";

export class User {
  constructor(
    public id: number,
    public username: string,
    public profileImgUrl: string
  ) { }
}

@Injectable({
  providedIn: "root",
})
export class UserAdapter implements Adapter<User> {
  adapt(item: any): User {
    return new User(item.id, item.username, item.profileImgUrl);
  }

  adaptArr(arr: any[]): User[] {
    return arr.map(item => this.adapt(item));
  }
}
