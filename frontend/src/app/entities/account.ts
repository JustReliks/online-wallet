import {Operation} from "./operation";

export class Account {

  private _name: string;
  private _currency: string;
  private _value: number;
  private _operations: Array<Operation>;

  constructor(name: string, value: number, currency: string) {
    this._value = value;
    this._currency = currency;
    this._name = name;
  }


  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get currency(): string {
    return this._currency;
  }

  set currency(value: string) {
    this._currency = value;
  }

  get value(): number {
    return this._value;
  }

  set value(value: number) {
    this._value = value;
  }
}
