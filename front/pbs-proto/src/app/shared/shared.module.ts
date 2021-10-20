import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from './services/post.service';
import { FeedComponent } from './components/feed/feed.component';
import { HeaderMainComponent } from './components/header-main/header-main.component';
import { PostComponent } from './components/post/post.component';
import { RouterModule } from '@angular/router';
import { ProfileService } from './services/profile.service';
import { AuthenticationService } from './services/auth/authentication.service';



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
    ProfileService,
    AuthenticationService
  ],
  exports: [
    FeedComponent,
    HeaderMainComponent,
    PostComponent
  ]
})
export class SharedModule { }
