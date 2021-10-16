import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeedRecentRoutingModule } from './feed-recent-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { FeedRecentComponent } from './feed-recent.component';


@NgModule({
  declarations: [FeedRecentComponent],
  imports: [
    CommonModule,
    FeedRecentRoutingModule,
    SharedModule
  ]
})
export class FeedRecentModule { }
