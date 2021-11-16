import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';

@Component({
  selector: 'app-oauth2callback',
  templateUrl: './oauth2callback.component.html',
  styleUrls: ['./oauth2callback.component.scss']
})
export class Oauth2callbackComponent  implements OnInit {


  isAuthenticating = true;
  isFailed = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit(): void {
    console.log('nginit oauth2callback');
    
    this.route.queryParams.subscribe( p => {
      const code = p.code;
      const state = p.state;
      if (code && state) {
        this.authenticationService.codeExchangeGoogle(p.code, p.state).subscribe(
          data => {
            this.router.navigate(['/recent']);
          },
          error => {
            this.router.navigate(['/auth']);
          }
        );
      } else {

      }
    });
    
  }

}
