import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/shared/models/post.model';
import { Roles } from 'src/app/shared/models/user.model';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';
import { PostService } from 'src/app/shared/services/post.service';
import { PostEditorComponent } from '../post-editor/post-editor.component';
import { PostEditService } from './post-edit.service';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-edit.component.html',
  styleUrls: ['./post-edit.component.scss']
})
export class PostEditComponent implements OnInit {

  @ViewChild("postEditor") 
  postEditor?: PostEditorComponent

  post?:Post;
  serverErrorEdit = false;
  serverErrorDelete = false;

  constructor(
    private activatedRoute : ActivatedRoute,
    private editPostService : PostEditService,
    private postService : PostService,
    private router : Router,
    private authService: AuthenticationService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(parameter => {
      const parameterPostId = parameter.parameter;
      this.postService.getPost(parameterPostId).subscribe(data => {
        if (!this.isOwnerOrHigher(data)) {
          this.router.navigate(["profile"]);
        }
        this.post = data;
        this.postEditor?.setTitleContent(data.title, data.content);
        
      });
    })
  }

  private isOwnerOrHigher(post: Post) : boolean {
    if (!this.authService.isAuthenticated) {
      return false;
    }

    //isOwner
    if (post.author.id === this.authService.authenticatedUserValue?.id) {
      return true;
    }
    //orHigher
    if (this.authService.authenticatedUserValue?.role === Roles.ADMIN) {
      return true;
    }

    return false;
  }

  editPostButtonClick() {
    this.serverErrorEdit = false;
    if (!this.postEditor?.validatePost()) {
      return;
    }
    if (!this.post?.id) {
      return;
    }

    this.editPostService.editPost(this.post?.id,this.postEditor?.getTitleValue(), this.postEditor?.getContentValue()).subscribe(
      value => {
        this.router.navigate([`/post/${this.post?.id}`]);
      }, 
      error => {
        this.serverErrorEdit = true;
        console.log('[Edit post] Error +' + error);
      }
    );
  }

  deletePostButtonClick() {
    this.serverErrorDelete = false;
    if (!this.post?.id) {
      return;
    }

    this.editPostService.deletePost(this.post?.id).subscribe(
      value => {
        this.router.navigate(['profile']);
      }, 
      error => {
        this.serverErrorEdit = true;
        console.log('[Delete post] Error ' + error);
      }
    );
  }

}
