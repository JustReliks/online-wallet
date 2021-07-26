export class Goal {

  value: number;
  name: string;
  date: string;
  completed: boolean;
  dailyPayment: number;

  constructor(json: any) {
    this.value = json?.value;
    this.name = json?.name;
    this.date = json?.date;
    this.completed = json?.completed;
    this.dailyPayment = json?.dailyPayment;
  }
}
