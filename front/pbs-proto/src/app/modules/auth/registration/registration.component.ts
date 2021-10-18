import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { RegistrationResponseDto, RegistrationResponseStatus } from './registration-response.dto';
import { RegistrationService } from './registration.service';
import { UserCredentialsDto } from './user-credentials.dto';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  readonly USERNAME_MIN_LENGTH = 4;
  readonly USERNAME_MAX_LENGTH = 32;

  readonly PASSWORD_MIN_LENGTH = 8;
  readonly PASSWORD_MAX_LENGTH = 32;


  //Errors from the registration response
  emailExists = false;
  usernameExists = false;
  badEmail = false;
  badUsername = false;
  badPassword = false;
  unknownError = false;

  isRegistrationSuccess = false;

  //Registration query status
  isPending = false;

  regForm = new FormGroup({
    email: new FormControl('', Validators.compose([Validators.required, Validators.email])),
    username: new FormControl('', Validators.compose([Validators.required, Validators.minLength(this.USERNAME_MIN_LENGTH), Validators.maxLength(this.USERNAME_MAX_LENGTH)])),
    password: new FormControl('', Validators.compose([Validators.required, Validators.minLength(this.PASSWORD_MIN_LENGTH), Validators.maxLength(this.PASSWORD_MAX_LENGTH)])),
    passwordRepeat: new FormControl('', Validators.compose([Validators.required])),
  }, this.passwordMatchValidator );

  constructor(
    private formBuilder: FormBuilder,
    private registrationService: RegistrationService
  ) {}


  ngOnInit(): void {
  }

  onSubmit(): void {
    if (!this.regForm.valid)
      return;
    
    if (this.isPending)
      return;
    this.isPending = true;
    this.registrationService.tryRegister(
      new UserCredentialsDto(
        this.regForm.controls['email'].value,
        this.regForm.controls['password'].value,
        this.regForm.controls['username'].value
      )
    ).subscribe(response => this.resolveRegistrationResult(response));
  }

  private resolveRegistrationResult(responseDto: RegistrationResponseDto) {
    this.resetAllRegistrationErrors();
    switch (responseDto.status) {
      case (RegistrationResponseStatus.SUCCESS):
        this.isRegistrationSuccess = true;
        break;
      case (RegistrationResponseStatus.EMAIL_EXISTS):
        console.log("exists email")
        this.emailExists = true;
        break;
      case (RegistrationResponseStatus.USERNAME_EXISTS):
        this.usernameExists = true;
        break;
      case (RegistrationResponseStatus.BAD_EMAIL):
        this.badEmail = true;
        break;
      case (RegistrationResponseStatus.BAD_PASSWORD):
        this.badPassword = true;
        break;
      case (RegistrationResponseStatus.BAD_USERNAME):
        this.badUsername = true;
        break;
      default:
        this.unknownError = true;
        break;
    }
    this.isPending = false;
  }

  private resetAllRegistrationErrors() {
    this.emailExists = false;
    this.usernameExists = false;
    this.badEmail = false;
    this.badUsername = false;
    this.badPassword = false;
    this.unknownError = false;
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    if (control == null)
      return null;
    const password: string = control.get('password')?.value;
    const confirmPassword: string = control.get('passwordRepeat')?.value;

    if (password !== confirmPassword) {
      return { 'nopwdmatch': true }
    }
    return null;
  }

}
