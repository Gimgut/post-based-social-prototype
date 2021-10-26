import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { map } from 'rxjs/operators';
import { CreatePostService } from './create-post.service';


@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {


  private readonly CONTENT_MIN_SIZE = 1;
  private readonly CONTENT_MAX_SIZE = 10000;

  createPostForm: FormGroup = this.formBuilder.group({
    title: ['', Validators.compose([Validators.required])]
  });

  postHtmlContent = '';
  contentMinSizeError = false;
  contentMaxSizeError = false;
  serverError = false;

  config1: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    minHeight: '10rem',
    placeholder: 'Enter text here...',
    translate: 'no',
    sanitize: true,
    // toolbarPosition: 'top',
    outline: true,
    showToolbar: true,
    defaultParagraphSeparator: 'p',
    toolbarHiddenButtons: [
      ['fontSize', 'textColor', 'backgroundColor', 'insertImage','insertHorizontalRule','heading'],
      ['fontName']
    ]
  };


  constructor(
    private formBuilder : FormBuilder,
    private createPostService : CreatePostService,
    private router : Router
  ) { }

  ngOnInit(): void {
  }

  getTitle() {
    return this.createPostForm.get('title');
  }

  createPostButtonClick() {
    console.log('enter..')
    this.contentMinSizeError = false;
    this.contentMaxSizeError = false;
    this.serverError = false;
    if (!this.createPostForm.valid) return;
    const content = this.postHtmlContent.trim();
    if (content.length < this.CONTENT_MIN_SIZE) { this.contentMinSizeError = true; return; }
    if (content.length > this.CONTENT_MAX_SIZE) { this.contentMaxSizeError = true; return; }
    this.createPostService.createPost(this.getTitle()?.value, content).subscribe(
      value => {
        this.router.navigate([`/post/${value}`]);
      }, 
      error => {
        this.serverError = true;
        console.log('[Create post] Server error');
      }
    );
    
  }

}
