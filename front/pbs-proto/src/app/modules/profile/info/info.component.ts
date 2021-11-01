import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user.model';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit {

  user: User|null;

  constructor(
    private authenticationService: AuthenticationService
  ) {
    this.user = authenticationService.authenticatedUserValue;
   }

  ngOnInit(): void {
  }

}
