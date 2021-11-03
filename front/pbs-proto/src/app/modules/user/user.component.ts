import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from 'src/app/shared/models/post.model';
import { User } from 'src/app/shared/models/user.model';
import { RecentFeedOfUserService } from 'src/app/shared/services/feed/recent-of-user.service';
import { UserService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  userInfo:User|null=null;
  posts:Post[]=[];

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private recentService: RecentFeedOfUserService
  ) { 
  }

  ngOnInit(): void {
    console.log('UserComponent onInit');
    this.activatedRoute.params.subscribe(parameter => {
      const userId:string = parameter.parameter;

      this.userService.getUserInfo(userId).subscribe(data => {
        this.userInfo = data;
      });

      this.recentService.setUserId(userId);
      this.posts = this.recentService.getPosts();
      this.recentService.tryFetchMore();
    });
  }
}
