import {Currency} from "./currency";

export class AccountBill {
    private _id: number;
    private _accountId: number;
    private _currency: Currency;
    private _balance: number;

    constructor(json: any) {
        this._id = json._id;
        this._accountId = json._accountId;
        this._currency = new Currency(json._currency);
        this._balance = json._balance;
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get accountId(): number {
        return this._accountId;
    }

    set accountId(value: number) {
        this._accountId = value;
    }

    get currency(): Currency {
        return this._currency;
    }

    set currency(value: Currency) {
        this._currency = value;
    }

    get balance(): number {
        return this._balance;
    }

    set balance(value: number) {
        this._balance = value;
    }
}
