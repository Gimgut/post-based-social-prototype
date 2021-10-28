import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InfoComponent } from './shared/components/info/info.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: InfoComponent},
      { path: 'recent', loadChildren: () => import('./modules/feed-recent/feed-recent.module').then(m => m.FeedRecentModule) },
      { path: 'auth', loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule) },
      { path: 'post/:parameter', loadChildren: () => import('./modules/post-page/post-page.module').then(m => m.PostPageModule) },
      { path: 'profile', loadChildren: () => import('./modules/profile/profile.module').then(m => m.ProfileModule) }
    ]
  }
];


@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
