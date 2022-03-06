import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeedRecentRoutingModule } from './feed-recent-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { FeedRecentComponent } from './feed-recent.component';
import { MatButtonModule } from '@angular/material/button';


@NgModule({
  declarations: [FeedRecentComponent],
  imports: [
    CommonModule,
    FeedRecentRoutingModule,
    SharedModule,
    MatButtonModule
  ]
})
export class FeedRecentModule { }
