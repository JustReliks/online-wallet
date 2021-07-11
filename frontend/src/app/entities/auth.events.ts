import {AuthUser} from './user';

export class LogoutEvent {
}

export class AuthSuccessEvent {
  public user: AuthUser;

  constructor(user: AuthUser) {
    this.user = user;
  }
}

export class AuthFailureEvent {
  public cause: string;

  constructor(cause: string) {
    this.cause = cause;
  }
}
