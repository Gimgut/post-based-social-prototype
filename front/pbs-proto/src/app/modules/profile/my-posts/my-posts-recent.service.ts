import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostAdapter } from 'src/app/shared/models/post.model';
import { ApiRoutes } from 'src/app/shared/services/api.routes';
import { RecentFeedOfUserService } from 'src/app/shared/services/feed/recent-of-user.service';

@Injectable({
  providedIn: 'root'
})
export class MyPostsRecentService extends RecentFeedOfUserService {

  constructor(
    protected http: HttpClient,
    protected postAdapter: PostAdapter,
    protected apiRoutes: ApiRoutes
  ) {
    super(http, postAdapter, apiRoutes);
  }
}
