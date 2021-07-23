export class Pair {
  public first: string;
  public second: number;

  constructor(json: any) {
      this.first = json.first;
      this.second = json.second;
  }

}
