import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/shared/services/security/authentication.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: FormGroup = this.formBuilder.group({
    email: ['', Validators.compose([Validators.required, Validators.email])],
    password: ['', Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(64)])]
  });
  
  successfulAuthenticationSubscription: any;
  failedAuthenticationSubscription: any;

  isAuthenticationFailed = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    public authenticationService: AuthenticationService
    ) { }

  ngOnInit(): void {
    this.successfulAuthenticationSubscription = this.authenticationService.successfulAuthenticationEvent
        .subscribe({ 
          next: i => {
            console.log('auth success event ' + i); 
            this.isAuthenticationFailed = false;
            this.router.navigate(['/recent']); 
          }
        });

      this.failedAuthenticationSubscription = this.authenticationService.failedAuthenticationEvent
        .subscribe({ 
          next: i => { 
            this.loginForm.enable();
            this.isAuthenticationFailed = true;
            console.log('auth failed event ' + i);
         }
        });
  }

  ngOnDestroy(): void {
    this.successfulAuthenticationSubscription?.unsubscribe();
    this.failedAuthenticationSubscription?.unsubscribe();
  }

  googleAuth() : void {
    this.authenticationService.tryGoogleAuth();
  }

  getEmail() : AbstractControl | null {
    return this.loginForm.get('email');
  }

  getPassword() : AbstractControl | null {
    return this.loginForm.get('password');
  }

  onSubmit(): void {
    if (!this.loginForm.valid) {
      return;
    }
    this.loginForm.disable();
    this.authenticationService.trySignIn_ByEmail(this.getEmail()?.value, this.getPassword()?.value);
  }

}

