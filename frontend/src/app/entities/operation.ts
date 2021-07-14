export enum OperationType {
  INCOME = 'INCOME', LOSS = 'LOSS'
}

export class Operation {
  private readonly _id: number;
  private readonly _type: OperationType;
  private readonly _value: number;
  private readonly _date: string;
  private readonly _description: string;
  private readonly _currency: string;



  constructor(id: number, type : OperationType, value: number, date: string, description: string, currency: string) {
    this._id = id;
    this._type = type;
    this._value = value;
    this._date = date;
    this._description = description;
    this._currency = currency;
  }


  get id(): number {
    return this._id;
  }

  get type(): OperationType {
    return this._type;
  }

  get value(): number {
    return this._value;
  }

  get date(): string {
    return this._date;
  }

  get description(): string {
    return this._description;
  }

  get currency(): string {
    return this._currency;
  }

}
