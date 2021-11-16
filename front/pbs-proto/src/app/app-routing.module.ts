import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InfoComponent } from './shared/components/info/info.component';
import { AuthenticatedOnlyService } from './shared/services/permission-checkers/authenticated-only.service';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: InfoComponent},
      { path: 'recent', loadChildren: () => import('./modules/feed-recent/feed-recent.module').then(m => m.FeedRecentModule) },
      { path: 'user/:parameter', loadChildren: () => import('./modules/user/user.module').then(m => m.UserModule) },
      { path: 'auth', loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule) },
      { path: 'post/:parameter', loadChildren: () => import('./modules/post-page/post-page.module').then(m => m.PostPageModule) },
      { path: 'profile', loadChildren: () => import('./modules/profile/profile.module').then(m => m.ProfileModule), canActivate: [AuthenticatedOnlyService] }
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
