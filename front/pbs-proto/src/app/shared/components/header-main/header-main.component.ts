import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/auth/authentication.service';


@Component({
  selector: 'app-header-main',
  templateUrl: './header-main.component.html',
  styleUrls: ['./header-main.component.scss']
})
export class HeaderMainComponent implements OnInit {
  constructor(
    public authenticationService: AuthenticationService
  ) {
   }

  ngOnInit(): void {
  }

  get authenticatedUser() {
    return this.authenticationService.authenticatedUserValue;
  }

}
