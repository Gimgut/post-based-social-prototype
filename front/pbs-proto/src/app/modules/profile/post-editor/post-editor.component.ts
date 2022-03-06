import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AngularEditorConfig } from '@kolkov/angular-editor';

@Component({
  selector: 'app-post-editor',
  templateUrl: './post-editor.component.html',
  styleUrls: ['./post-editor.component.scss']
})
export class PostEditorComponent implements OnInit {

  private readonly TITLE_MIN_SIZE = 1;
  private readonly TITLE_MAX_SIZE = 256;
  private readonly CONTENT_MIN_SIZE = 1;
  private readonly CONTENT_MAX_SIZE = 10000;

  createPostForm: FormGroup = this.formBuilder.group({
    title: ['', Validators.compose([
      Validators.required, 
      Validators.minLength(this.TITLE_MIN_SIZE), 
      Validators.maxLength(this.TITLE_MAX_SIZE)
    ])]
  });

  postHtmlContent = '';
  contentMinSizeError = false;
  contentMaxSizeError = false;

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
    private formBuilder : FormBuilder
  ) {}

  ngOnInit(): void {
  }

  setTitleContent(title:string, content:string) {
    this.getTitle()?.setValue(title);
    this.postHtmlContent = content;
  }

  getTitle() {
    return this.createPostForm.get('title');
  }

  getTitleValue() {
    return this.getTitle()?.value;
  }

  getContentValue() {
    return this.postHtmlContent;
  }

  validatePost() : boolean  {
    this.contentMinSizeError = false;
    this.contentMaxSizeError = false;
    if (!this.createPostForm.valid) return false;
    this.postHtmlContent = this.postHtmlContent.trim();
    if (this.postHtmlContent.length < this.CONTENT_MIN_SIZE) { this.contentMinSizeError = true; return false; }
    if (this.postHtmlContent.length > this.CONTENT_MAX_SIZE) { this.contentMaxSizeError = true; return false; }
    return true;
  }

}
