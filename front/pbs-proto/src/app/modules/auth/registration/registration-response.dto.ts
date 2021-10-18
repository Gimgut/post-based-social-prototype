import { Injectable } from "@angular/core";
import { Adapter } from "src/app/shared/models/adapter.model";

export enum RegistrationResponseStatus {
  SUCCESS = "SUCCESS", FAILED = "FAILED",
  EMAIL_EXISTS = "EMAIL_EXISTS", USERNAME_EXISTS = "USERNAME_EXISTS",
  BAD_EMAIL = "BAD_EMAIL", BAD_USERNAME = "BAD_USERNAME", BAD_PASSWORD = "BAD_PASSWORD"
}

export class RegistrationResponseDto {
  constructor(
    public status: RegistrationResponseStatus
  ) { }
}

@Injectable({
  providedIn: "root",
})
export class RegistrationResponseAdapter implements Adapter<RegistrationResponseDto> {
  adapt(item: any): RegistrationResponseDto {
    return new RegistrationResponseDto(item.status as RegistrationResponseStatus);
  }
}
