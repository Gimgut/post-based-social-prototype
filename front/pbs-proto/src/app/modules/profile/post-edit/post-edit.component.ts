import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/shared/models/post.model';
import { PostService } from 'src/app/shared/services/post.service';
import { PostEditorComponent } from '../post-editor/post-editor.component';
import { PostEditService } from './post-edit.service';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-edit.component.html',
  styleUrls: ['./post-edit.component.scss']
})
export class PostEditComponent implements OnInit {

  @ViewChild("postEditor") postEditor?: PostEditorComponent

  post?:Post;
  serverErrorEdit = false;
  serverErrorDelete = false;

  constructor(
    private activatedRoute : ActivatedRoute,
    private editPostService : PostEditService,
    private postService : PostService,
    private router : Router
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(parameter => {
      const parameterPostId = parameter.parameter;
      this.postService.getPost(parameterPostId).subscribe(data => {
        this.post = data;
        this.postEditor?.setTitleContent(data.title, data.content);
      });
    })
  }

  editPostButtonClick() {
    this.serverErrorEdit = false;
    if (!this.postEditor?.validatePost()) return;
    if (!this.post?.id) return;

    this.editPostService.editPost(this.post?.id,this.postEditor?.getTitleValue(), this.postEditor?.getContentValue()).subscribe(
      value => {
        this.router.navigate([`/post/${this.post?.id}`]);
      }, 
      error => {
        this.serverErrorEdit = true;
        console.log('[Edit post] Server error');
      }
    );
  }

  deletePostButtonClick() {
    this.serverErrorDelete = false;
    if (!this.post?.id) return;
    this.editPostService.deletePost(this.post?.id).subscribe(
      value => {
        this.router.navigate(['profile']);
      }, 
      error => {
        this.serverErrorEdit = true;
        console.log('[Delete post] Server error');
      }
    );
  }

}
