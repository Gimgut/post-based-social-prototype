import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './user.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { UserInfoComponent } from './user-info/user-info.component';
import { UserFeedRecentComponent } from './user-feed-recent/user-feed-recent.component';


@NgModule({
  declarations: [
    UserComponent,
    UserInfoComponent,
    UserFeedRecentComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    SharedModule
  ]
})
export class UserModule { }
