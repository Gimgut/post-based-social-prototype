import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { LoginResponseStatus } from 'src/app/shared/dto/auth/login-response.dto';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';
import { AuthenticationServiceOld } from 'src/app/shared/services/security/authentication-old.service';


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

  isAuthenticating = false;
  isAuthenticationFailed = false;
  isServerError = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    public authenticationService: AuthenticationService
    ) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  googleAuth() : void {
    this.authenticationService.loginWithGoogle();
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
    this.isAuthenticating = true;
    this.isAuthenticationFailed = false;
    this.isServerError = false;
    this.loginForm.disable();
    this.authenticationService.loginWithEmailPassword(this.getEmail()?.value, this.getPassword()?.value).subscribe(
      next => {
        if (next.status === LoginResponseStatus.SUCCESS) {
          this.isAuthenticationFailed = false;
          this.router.navigate(['/recent']); 
        } else {
          this.isAuthenticationFailed = true;
        }
      },
      error => {
        console.log('error: ' + error);
        this.isServerError = true;
        this.afterSubmit();
      },
      () => {
        //complete
        console.log('complete!');
        this.afterSubmit();
      });
  }

  private afterSubmit() {
    this.isAuthenticating = false;
    this.loginForm.enable();
  }

}

