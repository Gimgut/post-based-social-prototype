import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { ApiRoutes } from '../api.routes';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(
        private authenticationService: AuthenticationService,
        private apiRoutes: ApiRoutes
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const isLoggedIn = this.authenticationService.authenticatedUserValue && this.authenticationService.accessToken;
        const isApiUrl = request.url.startsWith(this.apiRoutes.apiUrl);
        if (isLoggedIn && isApiUrl) {
            request = request.clone({
                setHeaders: { Authorization: `Bearer ${this.authenticationService.accessToken}` }
            });
            console.log('header access token set')
        }

        return next.handle(request);
    }
}