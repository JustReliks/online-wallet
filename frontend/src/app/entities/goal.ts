export class Goal {

  value: number;
  name: string;
  date: string;

  constructor(json: any) {
    this.value = json?.value;
    this.name = json?.name;
    this.date = json?.date;
  }
}
