import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { Oauth2callbackComponent } from './oauth2callback/oauth2callback.component';
import { RegistrationComponent } from './registration/registration.component';

const routes: Routes = [
  {
    path: '',
    //component: AuthComponent,
    children: [
      { path: 'signup', component: RegistrationComponent },
      { path: '', component: LoginComponent },
      { path: 'oauth2/callback', component: Oauth2callbackComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
