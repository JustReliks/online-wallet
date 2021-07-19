export class Goal {

  private _value: number;
  private _name: string;


  get value(): number {
    return this._value;
  }

  set value(value: number) {
    this._value = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }
}
