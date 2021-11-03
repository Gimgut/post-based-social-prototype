import { Component, OnInit } from '@angular/core';
import { Roles, User } from 'src/app/shared/models/user.model';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  private profileInfo: User|null;

  constructor(
    private authService: AuthenticationService
  ) {
    this.profileInfo = authService.authenticatedUserValue; 
   }

  ngOnInit(): void {
  }

  canCreatePost(): boolean {
    return this.profileInfo?.role === Roles.WRITER
      || this.profileInfo?.role === Roles.ADMIN;
  }
}
