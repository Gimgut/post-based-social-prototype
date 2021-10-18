import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from 'src/app/shared/models/post.model';
import { PostService } from 'src/app/shared/services/post.service';

@Component({
  selector: 'app-post-page',
  templateUrl: './post-page.component.html',
  styleUrls: ['./post-page.component.scss']
})
export class PostPageComponent implements OnInit {

  public parameterId!: string;
  public post: Post = { id: 8, title: 'default post title', content: 'default post content', author_id: 0, author_name: 'no_author' };

  constructor(
    private activatedRoute: ActivatedRoute,
    private postService: PostService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(parameter => {
      this.parameterId = parameter.parameter
      //console.log(this.parameterId)
      this.postService.getPost(this.parameterId).subscribe(data => this.post = data);
    })
  }

}
