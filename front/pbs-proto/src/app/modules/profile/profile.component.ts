import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user.model';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user: User|null;

  constructor(
    private authenticationService: AuthenticationService
  ) {
    this.user = authenticationService.authenticatedUserValue;
    console.log('typeof date:' + typeof(this.user?.registrationTime));
   }

  ngOnInit(): void {
  }

  logout() {
    this.authenticationService.logout();
  }

}
