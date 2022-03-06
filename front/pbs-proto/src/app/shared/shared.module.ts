import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from './services/post.service';
import { FeedComponent } from './components/feed/feed.component';
import { HeaderMainComponent } from './components/header-main/header-main.component';
import { PostComponent } from './components/post/post.component';
import { RouterModule } from '@angular/router';
import { AuthenticationService } from './services/auth/authentication.service';
import { FlexLayoutModule } from "@angular/flex-layout";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatMenuModule} from '@angular/material/menu';
import { InfoComponent } from './components/info/info.component';
import { SubscriptionService } from './services/subscription.service';
import { SubscriptionFeedService } from './services/subscription-feed.service';
import { UserService } from './services/user.service';
import { SubButtonsComponent } from './components/subscription/sub-buttons/sub-buttons.component';



@NgModule({
  declarations: [
    FeedComponent,
    HeaderMainComponent,
    PostComponent,
    InfoComponent,
    SubButtonsComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    FlexLayoutModule
  ],
  exports: [
    FeedComponent,
    HeaderMainComponent,
    PostComponent,
    SubButtonsComponent
  ]
})
export class SharedModule {

  static forRoot() {
    return {
      ngModule: SharedModule,
      providers: [
        PostService,
        AuthenticationService, 
        SubscriptionService,
        SubscriptionFeedService,
        UserService
      ]
    }
  }
  
}
