import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePostComponent } from './create-post/create-post.component';
import { PostEditComponent } from './post-edit/post-edit.component';
import { ProfileComponent } from './profile.component';

const routes: Routes = [
  {
    path: '', 
    component: ProfileComponent,
    children : [
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
