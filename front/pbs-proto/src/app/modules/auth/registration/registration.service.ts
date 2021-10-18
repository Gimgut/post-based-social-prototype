import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiRoutes } from 'src/app/shared/services/api.routes';
import { RegistrationResponseAdapter, RegistrationResponseDto } from './registration-response.dto';
import { UserCredentialsDto } from './user-credentials.dto';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(
    private http: HttpClient,
    private apiRoutes: ApiRoutes,
    @Inject(RegistrationResponseAdapter) private regResponseAdapter: RegistrationResponseAdapter
  ) { }

  tryRegister(userCredentials: UserCredentialsDto): Observable<RegistrationResponseDto> {
    return this.http.post(this.apiRoutes.createNewAccount(), userCredentials).pipe(map(item => this.regResponseAdapter.adapt(item)));
  }
}