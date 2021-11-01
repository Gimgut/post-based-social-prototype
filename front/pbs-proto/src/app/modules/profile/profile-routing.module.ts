import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePostComponent } from './create-post/create-post.component';
import { InfoComponent } from './info/info.component';
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
      {path: 'create/post', component: CreatePostComponent},
      {path: 'edit/post/:parameter', component: PostEditComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
