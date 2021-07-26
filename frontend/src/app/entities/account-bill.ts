import {Currency} from "./currency";

export class AccountBill {
  public id: number;
  public accountId: number;
  public currency: Currency;
  public startBalance: number;
  public balance: number;
  public rate: number;
  public maturityDate: string;

  constructor(json: any) {
    this.id = json.id;
    this.accountId = json.accountId;
    this.currency = new Currency(json.currency);
    this.startBalance = json.startBalance;
    this.balance = json.balance;
    this.rate = json.rate;
    this.maturityDate = json.maturityDate;
  }
}
