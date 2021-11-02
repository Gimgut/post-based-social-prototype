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
import { InfoComponent } from './info/info.component';
import { SubscriptionsComponent } from './subscriptions/subscriptions.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatMenuModule } from '@angular/material/menu';


@NgModule({
  declarations: [
    ProfileComponent,
    CreatePostComponent,
    PostEditorComponent,
    PostEditComponent,
    InfoComponent,
    SubscriptionsComponent
  ],
  imports: [
    CommonModule,
    ProfileRoutingModule,
    SharedModule,

    AngularEditorModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatMenuModule
  ]
})
export class ProfileModule { }
