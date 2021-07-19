import {AccountBill} from "./account-bill";
import {Goal} from "./goal";

export class Account {

  public id: number;
  public userId: number;
  public name: string;
  public description: string;
  public createdAt: string;
  public goal: Goal;
  public lastTransaction: string;
  public icon: string;
  public accountBills: Array<AccountBill>;

  constructor(json: any) {
    this.id = json.id;
    this.userId = json.userId;
    this.name = json.name;
    this.description = json.description;
    this.goal=json.goal;
    this.createdAt = json.createdAt;
    this.lastTransaction = json.lastTransaction;
    this.icon = json.icon;
    this.accountBills = json?.accountBills?.map(bill => new AccountBill(bill))
  }
}
