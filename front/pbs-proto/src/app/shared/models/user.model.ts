import { Injectable } from "@angular/core";
import { DateAdapter } from "../dto/date.adapter";
import { Adapter } from "./adapter.model";

export class User {
  constructor(
    public id: number,
    public username: string,
    public picture: string,
    public role: string,
    public registrationTime: Date
  ) { 
  }
}


@Injectable({
  providedIn: "root",
})
export class UserAdapter implements Adapter<User> {

  constructor(
    private dateAdapter: DateAdapter
  ) {}

  adapt(item: any): User {
    const dtoDate = new Date(item.registrationTime);
    return new User(item.id, item.username, item.picture, item.role, this.dateAdapter.adapt(item.registrationTime));
  }

  adaptArr(arr: any[]): (User)[] {
    return arr.map(item => this.adapt(item));
  }
}

