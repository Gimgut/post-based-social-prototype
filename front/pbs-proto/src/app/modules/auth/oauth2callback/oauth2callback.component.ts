import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-oauth2callback',
  templateUrl: './oauth2callback.component.html',
  styleUrls: ['./oauth2callback.component.scss']
})
export class Oauth2callbackComponent  implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit(): void {
    console.log('nginit oauth2callback');
    this.route.queryParams.subscribe( p => {
      this.fetchToken(p.code, p.state).subscribe(data => {
        console.log('FETCH TOKEN CALL RESULT = ' + (data as any).userInfo.username);
      })
    });
  }

  fetchToken(code: string, state: string) {
    const tokenEndpoint = 'http://localhost:8080/login/oauth2/code/google';
    return this.http.get(tokenEndpoint+'?code='+code+'&state='+state);
  }

}
