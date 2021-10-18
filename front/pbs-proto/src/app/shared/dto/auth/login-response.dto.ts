import { Injectable } from "@angular/core";
import { Adapter } from "../../models/adapter.model";
import { User, UserAdapter } from "../../models/user.model";

export enum LoginResponseStatus {
  SUCCESS = "SUCCESS", FAILED = "FAILED"
}

export class LoginResponseDto {
  constructor(
    public status: LoginResponseStatus,
    public userInfo: User
  ) { }
}

@Injectable({
  providedIn: "root",
})
export class LoginResponseAdapter implements Adapter<LoginResponseDto> {

  constructor(private userAdapter: UserAdapter) {

  }

  adapt(item: any): LoginResponseDto {
    return new LoginResponseDto(item.status as LoginResponseStatus, this.userAdapter.adapt(item.userInfo == undefined ? '' : item.userInfo));
  }
}
