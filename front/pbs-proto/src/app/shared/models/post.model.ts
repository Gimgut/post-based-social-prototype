import { Injectable } from "@angular/core";
import { Adapter } from "./adapter.model";

export class Post {
  constructor(
    public id: number,
    public title: string,
    public content: string,

    public author_id: number,
    public author_name: string
  ) {}
}

@Injectable({
  providedIn: "root",
})
export class PostAdapter implements Adapter<Post> {
  adapt(item: any): Post {
    return new Post(item.id, item.title, item.content, item.author_id ?? 'NOT_FOUND', item.author_name ?? 'NOT_FOUND');
  }

  adaptArr(arr: any[]): Post[] {
    return arr.map(item => this.adapt(item));
  }
}
