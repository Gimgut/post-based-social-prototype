import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SubButtonsComponent } from 'src/app/shared/components/subscription/sub-buttons/sub-buttons.component';
import { Post } from 'src/app/shared/models/post.model';
import { User, Roles } from 'src/app/shared/models/user.model';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';
import { PostService } from 'src/app/shared/services/post.service';
import { SubscriptionService } from 'src/app/shared/services/subscription.service';

@Component({
  selector: 'app-post-page',
  templateUrl: './post-page.component.html',
  styleUrls: ['./post-page.component.scss']
})
export class PostPageComponent implements OnInit {

  public parameterId!: string;
  public post: Post|null = null;

  @ViewChild("subButtons") subButtons?: SubButtonsComponent;

  constructor(
    private activatedRoute: ActivatedRoute,
    private postService: PostService,
    private authService: AuthenticationService,
  ) { 
  }

  canEdit() : boolean {
    if (this.authService.authenticatedUserValue?.id === this.post?.author.id 
      || this.authService.authenticatedUserValue?.role === Roles.ADMIN) {
       return true;
    } else {
      return false;
    }
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(parameter => {
      this.parameterId = parameter.parameter;
      this.postService.getPost(this.parameterId).subscribe(data => {
        this.post = data;
        this.subButtons?.setAuthor(data.author);
      });
    });
  }

}
