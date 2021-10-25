import { Injectable } from "@angular/core";
import { Adapter } from "../../models/adapter.model";
import { User, UserAdapter } from "../../models/user.model";

export enum LoginResponseStatus {
  SUCCESS = "SUCCESS", FAILED = "FAILED"
}

export class LoginResponseDto {
  constructor(
    public status: LoginResponseStatus,
    public accessToken?: string,
    public refreshToken?: string,
    public userInfo?: User
  ) { }
}


@Injectable({
  providedIn: "root",
})
export class LoginResponseAdapter implements Adapter<LoginResponseDto> {

  constructor(private userAdapter: UserAdapter) {

  }

  adapt(item: any): LoginResponseDto {
    const status = item.status as LoginResponseStatus;
    if (status === LoginResponseStatus.FAILED) {
      return new LoginResponseDto(status);
    }
    return new LoginResponseDto(status, 
      item.accessToken,
      item.refreshToken,
      this.userAdapter.adapt(item.userInfo));
  }
}
