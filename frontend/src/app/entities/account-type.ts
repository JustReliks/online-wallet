import {Type} from "./type";

export class AccountType {
  public id: number;
  public accountId: number;
  public type: Type;

  constructor(json: any) {
    this.id = json.id;
    this.accountId = json.accountId;
    this.type = json.type;
  }
}
