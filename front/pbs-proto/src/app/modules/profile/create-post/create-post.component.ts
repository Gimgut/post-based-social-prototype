import { Component, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { map } from 'rxjs/operators';
import { PostEditorComponent } from '../post-editor/post-editor.component';
import { CreatePostService } from './create-post.service';


@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {

  @ViewChild("postEditor") postEditor?: PostEditorComponent

  serverError = false;

  constructor(
    private createPostService : CreatePostService,
    private router : Router
  ) { }

  ngOnInit(): void {
  }

  createPostButtonClick() {
    this.serverError = false;
    if (!this.postEditor?.validatePost()) {
      return;
    }
    //console.log("title text = " + this.postEditor?.getTitleValue());
    //console.log("content text = " + this.postEditor?.getContentValue());
    
    this.createPostService.createPost(this.postEditor?.getTitleValue(), this.postEditor?.getContentValue()).subscribe(
      value => {
        this.router.navigate([`/post/${value}`]);
      }, 
      error => {
        this.serverError = true;
        console.log('[Create post] Failed');
      }
    );
  }

}
