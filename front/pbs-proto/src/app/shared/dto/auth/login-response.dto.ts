import { Injectable } from "@angular/core";
import { Adapter } from "../../models/adapter.model";
import { User, UserAdapter } from "../../models/user.model";

export class LoginResponseDto {
  constructor(
    public accessToken: string,
    public refreshToken: string,
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
    return new LoginResponseDto(
      item.accessToken,
      item.refreshToken,
      this.userAdapter.adapt(item.userInfo));
  }
}
