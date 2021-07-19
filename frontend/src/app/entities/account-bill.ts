import {Currency} from "./currency";

export class AccountBill {
    public id: number;
    public accountId: number;
    public currency: Currency;
    public balance: number;

    constructor(json: any) {
      console.log(json)
        this.id = json.id;
        this.accountId = json.accountId;
        this.currency = new Currency(json.currency);
        this.balance = json.balance;
    }
}
