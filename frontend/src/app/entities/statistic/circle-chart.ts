import {Pair} from "../pair";

export class CircleChart {

  public data: Array<Pair>;

  constructor(json: any) {
      this.data = json.data;
  }

}
