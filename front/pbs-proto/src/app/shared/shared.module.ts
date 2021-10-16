import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from './services/post.service';
import { ProfileContainer } from './services/profile.container';
import { FeedComponent } from './components/feed/feed.component';
import { HeaderMainComponent } from './components/header-main/header-main.component';
import { PostComponent } from './components/post/post.component';
import { RouterModule } from '@angular/router';
import { Post } from './models/post.model';



@NgModule({
  declarations: [
    FeedComponent,
    HeaderMainComponent,
    PostComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  providers: [
    PostService,
    ProfileContainer
  ],
  exports: [
    FeedComponent,
    HeaderMainComponent,
    PostComponent
  ]
})
export class SharedModule { }
