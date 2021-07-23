export class LineChart {

  public categories: Array<String>;
  public seriesData: Array<number>;

  constructor(json: any) {
    this.categories = json.categories;
    this.seriesData = json.seriesData;
  }

}
