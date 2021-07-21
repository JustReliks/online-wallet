import {Account} from "./account";
import {AccountBill} from "./account-bill";

export class Transaction {
  id: number;
  account: Account;
  accountBill: AccountBill;
  categoryId: number;
  dateTime: string;
  quantity: number;

  constructor(json: any) {
    this.id = json.id;
    this.account = json.account;
    this.accountBill = json.accountBill;
    this.categoryId = json.categoryId;
    this.dateTime = json.dateTime;
    this.quantity = json.quantity;
  }
}
