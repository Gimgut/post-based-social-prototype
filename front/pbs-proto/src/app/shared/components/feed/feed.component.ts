import { Component, Input, OnInit } from '@angular/core';
import { Post } from '../../models/post.model';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {

  @Input()
  posts!: Post[];

  constructor() { }

  ngOnInit(): void {
  }

  addPost(post: Post): void {
    this.posts.push(post);
  }

  addPosts(posts: Post[]): void {
    this.posts.push(...posts);
  }

}
