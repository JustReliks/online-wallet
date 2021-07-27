import {AccountBill} from "./account-bill";
import {Goal} from "./goal";
import {ConvertedBalance} from "./converted-balance";
import {AccountType} from "./account-type";
import {CreditInfo} from "./credit-info";

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
  public convertedBalance: ConvertedBalance;
  public accountType: AccountType
  public freezeDate: string;
  public creditInfo: CreditInfo
  public maxBalance: number;

  constructor(json: any) {
    this.id = json.id;
    this.userId = json.userId;
    this.name = json.name;
    this.description = json.description;
    this.goal = json.goal;
    this.createdAt = json.createdAt;
    this.lastTransaction = json.lastTransaction;
    this.icon = json.icon;
    this.accountBills = json?.accountBills?.map(bill => new AccountBill(bill));
    this.convertedBalance = json?.convertedBalance;//new ConvertedBalance(json?.convertedBalance.balance, json.convertedBalance.currency);
    this.accountType = json?.accountType;
    this.freezeDate = json?.freezeDate;
    this.creditInfo = json?.creditInfo;
    this.maxBalance = json.maxBalance;
  }


}
