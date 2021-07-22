import {Account} from "./account";
import {AccountBill} from "./account-bill";
import {TransactionCategory} from "./transaction-category";

export class Transaction {
  id: number;
  account: Account;
  accountBill: AccountBill;
  dateTime: string;
  quantity: number;
  category: TransactionCategory;

  constructor(json: any) {
    this.id = json.id;
    this.account = json.account;
    this.accountBill = json.accountBill;
    this.dateTime = json.dateTime;
    this.quantity = json.quantity;
    this.category = json.category;
  }
}
