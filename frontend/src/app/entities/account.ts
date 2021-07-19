import {AccountBill} from "./account-bill";
import {Goal} from "./goal";

export class Account {

  private _id: number;
  private _userId: number;
  private _goal: Goal;
  private _name: string;
  private _description: string;
  private _createdAt: string;
  private _lastTransaction: string;
  private _icon: string;
  private _accountBills: Array<AccountBill>;

  constructor(json: any) {
    this._id = json._id;
    this._userId = json._userId;
    this._name = json._name;
    this._description = json._description;
    this._createdAt = json._createdAt;
    this._lastTransaction = json._lastTransaction;
    this._icon = json._icon;
    this._accountBills = json._accountBills.map(bill => new AccountBill(bill))
  }

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get userId(): number {
    return this._userId;
  }

  set userId(value: number) {
    this._userId = value;
  }
  get goal(): Goal {
    return this._goal;
  }

  set goal(value: Goal) {
    this._goal = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get description(): string {
    return this._description;
  }

  set description(value: string) {
    this._description = value;
  }

  get createdAt(): string {
    return this._createdAt;
  }

  set createdAt(value: string) {
    this._createdAt = value;
  }

  get lastTransaction(): string {
    return this._lastTransaction;
  }

  set lastTransaction(value: string) {
    this._lastTransaction = value;
  }

  get icon(): string {
    return this._icon;
  }

  set icon(value: string) {
    this._icon = value;
  }

  get accountBills(): Array<AccountBill> {
    return this._accountBills;
  }

  set accountBills(value: Array<AccountBill>) {
    this._accountBills = value;
  }
}
