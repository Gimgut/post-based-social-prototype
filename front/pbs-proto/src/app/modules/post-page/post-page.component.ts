import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  public post: Post|null = null;//= { id: 8, title: 'default post title', content: 'default post content' , author: new User(0, 'NOT_FOUND', '', 'NONE', new Date(0)) };

  public authorized:boolean;
  public isSubscribed:boolean = false;
  public subscriptionStatusDefined = true;


  constructor(
    private activatedRoute: ActivatedRoute,
    private postService: PostService,
    private authService: AuthenticationService,
    private subscriptionService: SubscriptionService
  ) { 
    this.authorized = authService.authenticatedUserValue? true : false;
  }

  canEdit() : boolean {
    if (this.authService.authenticatedUserValue?.id === this.post?.author.id 
      || this.authService.authenticatedUserValue?.role === Roles.ADMIN)
       return true;
    else
      return false;
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(parameter => {
      this.parameterId = parameter.parameter;
      //console.log(this.parameterId)
      this.postService.getPost(this.parameterId).subscribe(data => {
        this.post = data;
        this.updateSubscriptionStatus(this.post.author.id);
      });
    })
  }

  private updateSubscriptionStatus(idPublisher: number) {
    this.isSubscribed = this.subscriptionService.checkIsSubscribed(idPublisher);
    this.subscriptionStatusDefined = true;
  }

  subscribe() {
    const author = this.post?.author;
    if (!author) {
      return;
    }
    this.subscriptionService.subscribe(author)?.subscribe(
      data => {
        //this.subscriptionService.addSubscription(author);
        this.updateSubscriptionStatus(author.id);
      },
      error => {

      }
    );
  }

  unsubscribe() {
    const author = this.post?.author;
    if (!author) {
      return;
    }
    this.subscriptionService.unsubscribe(author)?.subscribe(
      data => {
        //this.subscriptionService.removeSubscription(author);
        this.updateSubscriptionStatus(author.id);
      },
      error => {

      }
    );
  }

}
