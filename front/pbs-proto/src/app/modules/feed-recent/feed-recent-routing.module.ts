import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FeedRecentComponent } from './feed-recent.component';

const routes: Routes = [
  {
    path: '',
    component: FeedRecentComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeedRecentRoutingModule { }
