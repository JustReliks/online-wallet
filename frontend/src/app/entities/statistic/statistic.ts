import {LineChart} from "./line-chart";
import {CircleChart} from "./circle-chart";

export class Statistics {

  public incomeLineChart: LineChart;
  public incomeCircleChart: CircleChart;
  public expenseLineChart: LineChart;
  public expenseCircleChart: CircleChart;

  constructor(json: any) {
    this.expenseCircleChart = json.expenseCircleChart;
    this.expenseLineChart = json.expenseLineChart;
    this.incomeCircleChart = json.incomeCircleChart;
    this.incomeLineChart = json.incomeLineChart;
  }

}
