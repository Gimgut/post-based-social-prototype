import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginResponseStatus } from 'src/app/shared/dto/auth/login-response.dto';
import { ApiRoutes } from 'src/app/shared/services/api.routes';
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
    /*
    const params = this.route.snapshot.params;
    this.fetchToken(params.code, params.state).subscribe(data => {
      console.log('FETCH TOKEN CALL RESULT = ' + (data as any).userInfo.username);
    });
    */
    
    this.route.queryParams.subscribe( p => {
      const code = p.code;
      const state = p.state;
      if (code && state) {
        this.authenticationService.authenticateWithGoogle(p.code, p.state).subscribe(
          data => {
            if (data.status === LoginResponseStatus.SUCCESS) {
              this.router.navigate(['/recent']);
            } else {
              this.router.navigate(['/auth']);
            }
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
