export class TransactionCategory {
  public id: number;
  public code: string;
  public title: string;
  public type: string;
  public icon: string;

  constructor(json: any) {
    this.id = json.id;
    this.code = json.code;
    this.title = json.title;
    this.type = json.type;
    this.icon = json.icon;
  }
}

export enum CategoryType {
  INCOME = "INCOME", EXPENSES = "EXPENSES"
}
