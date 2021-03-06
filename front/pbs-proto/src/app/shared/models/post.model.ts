import { Injectable } from "@angular/core";
import { Adapter } from "./adapter.model";
import { User, UserAdapter } from "./user.model";

export class Post {
  constructor(
    public id: number,
    public title: string,
    public content: string,
    public rating: number,
    public createdAt: Date,
    public author: User
  ) {}
}

@Injectable({
  providedIn: "root",
})
export class PostAdapter implements Adapter<Post> {

  constructor(
    private userAdapter: UserAdapter
  ) {}

  adapt(item: any): Post {
    return new Post(item.id, item.title, item.content, 
      item.rating,
      item.createdAt,
      this.userAdapter.adapt(item.author));
  }

  adaptArr(arr: any[]): Post[] {
    return arr.map(item => this.adapt(item));
  }
}
