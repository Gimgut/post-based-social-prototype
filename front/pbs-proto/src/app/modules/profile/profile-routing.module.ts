import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WriterOrHigherService } from 'src/app/shared/services/permission-checkers/writer-or-higher.service';
import { CreatePostComponent } from './create-post/create-post.component';
import { InfoComponent } from './info/info.component';
import { MyPostsComponent } from './my-posts/my-posts.component';
import { PostEditComponent } from './post-edit/post-edit.component';
import { ProfileComponent } from './profile.component';
import { SubscriptionsComponent } from './subscriptions/subscriptions.component';

const routes: Routes = [
  {
    path: '', 
    component: ProfileComponent,
    children : [
      {path: '', component: InfoComponent},
      {path: 'subscriptions', component: SubscriptionsComponent},
      {path: 'posts', component: MyPostsComponent},
      {path: 'create/post', component: CreatePostComponent, canActivate: [WriterOrHigherService] },
      {path: 'edit/post/:parameter', component: PostEditComponent, canActivate: [WriterOrHigherService] } //Ownership checked inside the component
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
