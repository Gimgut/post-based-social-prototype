import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { SubButtonsComponent } from 'src/app/shared/components/subscription/sub-buttons/sub-buttons.component';
import { User } from 'src/app/shared/models/user.model';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {

  @Input()
  user:User|null = null;

  constructor() { }

  ngOnInit(): void {
  }

}
