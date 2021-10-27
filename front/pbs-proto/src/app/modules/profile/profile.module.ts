import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProfileRoutingModule } from './profile-routing.module';
import { ProfileComponent } from './profile.component';
import { CreatePostComponent } from './create-post/create-post.component';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { PostEditorComponent } from './post-editor/post-editor.component';
import { PostEditComponent } from './post-edit/post-edit.component';


@NgModule({
  declarations: [
    ProfileComponent,
    CreatePostComponent,
    PostEditorComponent,
    PostEditComponent
  ],
  imports: [
    CommonModule,
    ProfileRoutingModule,

    AngularEditorModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class ProfileModule { }
