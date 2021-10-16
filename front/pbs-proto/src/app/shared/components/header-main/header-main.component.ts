import { Component, OnInit } from '@angular/core';
import { ProfileContainer } from '../../services/profile.container';

@Component({
  selector: 'app-header-main',
  templateUrl: './header-main.component.html',
  styleUrls: ['./header-main.component.scss']
})
export class HeaderMainComponent implements OnInit {

  constructor(
    public profileContainer: ProfileContainer
  ) { }

  ngOnInit(): void {
  }

}
