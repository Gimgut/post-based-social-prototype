import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthModule } from './modules/auth/auth.module';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: 'recent', loadChildren: () => import('./modules/feed-recent/feed-recent.module').then(m => m.FeedRecentModule) },
      { path: 'auth', loadChildren: () => import('./modules/auth/auth.module').then(m => AuthModule) }
    ]
  }
];


@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
