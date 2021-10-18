import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostPageComponent } from './post-page.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { PostPageRoutingModule } from './post-page-routing.module';


@NgModule({
  declarations: [
    PostPageComponent
  ],
  imports: [
    CommonModule,
    PostPageRoutingModule,
    SharedModule
  ]
})
export class PostPageModule { }
