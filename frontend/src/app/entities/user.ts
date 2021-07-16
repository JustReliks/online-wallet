import _ from 'lodash';

export enum UserRoleEnum {
  ADMIN = 'ROLE_ADMIN', USER = 'ROLE_USER'
}

export interface UserLight {
  id: number;
  username: string;
  email: string;
  balance: number;
  uuid: string;
  createdAt: string;
  twoFactorEnabled: boolean;
  userXenforoDto: UserXenforo;
}

export interface Role {
  id: string;
  name: string;
}

export interface Privilege {
  id: number;
  code: string;
  title: string;
  price: number;
  discount: number;
}


export interface UserRole extends Role {
  permissions: Array<string>;
}

export interface User extends UserLight {
  roles: Array<UserRole>;
}

export interface UserXenforo {
  id: number;
  url: string;
}

export class AuthUser implements User {

  constructor(json: any) {
    this.id = json.id;
    this.username = json.username;
    this.email = json.email;
    this.balance = json.balance;
    this.uuid = json.uuid;
    this.createdAt = json.createdAt;
    this.token = json.token;
    this.twoFactorEnabled = json.twoFactorEnabled;
    this.userXenforoDto = json.userXenforoDto;
    this.currency=json.currency;
  }

  id: number;
  username: string;
  email: string;
  balance: number;
  uuid: string;
  createdAt: string;
  token: string;
  twoFactorEnabled: boolean;
  roles: Array<UserRole>;
  userXenforoDto: UserXenforo;
  currency:string;

  static updateFromUserLight(user: AuthUser, userLight: any) {
    user.id = userLight.id;
    user.uuid = userLight.uuid;
    user.username = userLight.username;
    user.email = userLight.email;
    user.balance = userLight.balance;
    user.createdAt = userLight.createdAt;
    user.twoFactorEnabled = userLight.twoFactorEnabled;
    user.userXenforoDto = userLight.userXenforoDto;
  }

  public getRole(roleName: string) {
    return _.find(this.roles, (role: { name: string; }) => role.name === roleName);
  }
}
