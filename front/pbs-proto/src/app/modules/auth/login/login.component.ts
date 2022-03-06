import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/shared/services/auth/authentication.service';


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

  hidePassword = true;

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
      data => {
        this.isAuthenticationFailed = false;
        this.router.navigate(['/recent']); 
      },
      error => {
        console.log('error: ' + error);
        this.isAuthenticationFailed = true;
        this.releaseForm();
      },
      () => {
        console.log('complete!');
        this.releaseForm();
      });
  }

  private releaseForm() {
    this.isAuthenticating = false;
    this.loginForm.enable();
  }

}

