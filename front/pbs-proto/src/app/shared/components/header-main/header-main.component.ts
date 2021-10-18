import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';


@Component({
  selector: 'app-header-main',
  templateUrl: './header-main.component.html',
  styleUrls: ['./header-main.component.scss']
})
export class HeaderMainComponent implements OnInit {

  constructor(
    public profileService: ProfileService
  ) { }

  ngOnInit(): void {
  }

}
