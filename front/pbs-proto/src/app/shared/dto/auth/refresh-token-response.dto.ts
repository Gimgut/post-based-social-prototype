import { Injectable } from "@angular/core";
import { Adapter } from "../../models/adapter.model";
import { User, UserAdapter } from "../../models/user.model";

export enum RefreshTokenResponseStatus {
  SUCCESS = "SUCCESS", FAILED = "FAILED"
}

export class RefreshTokenResponseDto {
  constructor(
    public status: RefreshTokenResponseStatus,
    public userInfo: User,
    public accessToken: string,
    public refreshToken: string
  ) { }
}

/*
@Injectable({
  providedIn: "root",
})
export class RefreshTokenResponseAdapter implements Adapter<RefreshTokenResponseDto> {

  constructor(private userAdapter: UserAdapter) {

  }

  adapt(item: any): RefreshTokenResponseDto {
    return new RefreshTokenResponseDto(
        item.status as RefreshTokenResponseStatus, 
        this.userAdapter.adapt(item.userInfo == undefined ? '' : item.userInfo),
        item.accessToken as string,
        item.refreshToken as string
    );
  }
}
*/