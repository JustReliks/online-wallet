import {Component, Input, OnInit} from '@angular/core';
import {AuthUser} from "../../../../entities/user";
import _ from "lodash";
import {MatDialog} from "@angular/material/dialog";
import {Operation} from "../../../../entities/operation";
import {Account} from "../../../../entities/account";
import {DomSanitizer} from "@angular/platform-browser";
import {Chart} from 'angular-highcharts';
import {StatisticsService} from "../../../../service/statistics.service";
import {Statistics} from "../../../../entities/statistic/statistic";
import {AccountService} from "../../../../service/account.service";

@Component({
  selector: 'app-accounts-statistic',
  templateUrl: './accounts-statistic.component.html',
  styleUrls: ['./accounts-statistic.component.scss']
})
export class AccountsStatisticComponent implements OnInit {
  private _currentUser: any;
  private _originalUser: any;
  private _selectedAccount: Account;
  private _operations: any;
  private _accounts: Account[];
  height: number;
  width: number;
  private selectedDays: number=1;
  private _statistics: Statistics;

  @Input('user') set user(user: AuthUser) {
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  @Input('accounts') set accounts(accounts: Array<Account>) {
    this._accounts = accounts;
  }


  get user() {
    return this._currentUser;
  }

  get statistics(): Statistics {
    return this._statistics;
  }

  displayedColumns: string[] = ['id', 'date', 'value', 'currency', 'description']
  accountsExpanded: boolean = true;

  constructor(private dialog: MatDialog,
              private _sanitizer: DomSanitizer,
              private statisticsService: StatisticsService,
              private accountService: AccountService) {
    this._accounts = new Array<Account>();
    this.accountService.updateAccountsSubjectObservable.subscribe(res => {
      if (this.selectedAccount != null && this.selectedDays != null && res.updateStatistics) {
        this.statisticsService.getAccountStatistic(this.selectedAccount.id, this.selectedDays).subscribe(res => {
          this._statistics = res;
          this.initCharts(res, this.selectedDays)
        });
      }
    })
  }

  get accounts(): Array<Account> {
    return this._accounts;
  }

  isAccountSelected(): boolean {
    return this._selectedAccount != null;
  }


  get operations(): Array<Operation> {
    return this._operations;
  }

  set operations(value: Array<Operation>) {
    this._operations = value;
  }


  get selectedAccount(): Account {
    return this._selectedAccount;
  }

  set selectedAccount(value: Account) {
    this._selectedAccount = value;
  }

  ngOnInit(): void {
  }

  selectAccount($event: any) {
    this._selectedAccount = $event;
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }

  getAccountImg(account: Account) {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${account?.accountType?.type?.icon}`);
  }

  showAccountStatistic(account: Account) {
    this.accountsExpanded = false;
    this.selectedAccount = account;
    this.statisticsService.getAccountStatistic(account.id, 1).subscribe(res => {
      this._statistics = res;
      this.initCharts(res);
    });
  }

  private initCharts(res: Statistics, days: number = 1) {
    function getDaysName(days: number) {
      if (days == 1) {
        return '??????????';
      }
      if (days > 1) {
        return days + ' ????????'
      }
    }

    this.chartIncomeLine.ref.update({
      xAxis: {
        categories: res.incomeLineChart.categories as any
      },
      title: {
        text: '???????????? ?????????????? ???? ' + getDaysName(days)
      },
      series: [
        {
          name: '???????????? ???? ' + getDaysName(days),
          type: 'spline',
          data: res.incomeLineChart.seriesData as any
        }
      ]
    });
    this.chartExpensesLine.ref.update({
      xAxis: {
        categories: res.expenseLineChart.categories as any
      },
      title: {
        text: '???????????? ???????????????? ???? ' + getDaysName(days)
      },
      series: [
        {
          name: '?????????????? ???? ' + getDaysName(days),
          type: 'spline',
          data: res.expenseLineChart.seriesData as any
        }
      ]
    });

    this.chartIncomeCircle.ref.update({
      title: {
        text: '???????????? ???? ???????????????????? ???? ' + getDaysName(days)
      },
      series: [{
        type: 'pie',
        data: res.incomeCircleChart.data as any
      }]
    })

    this.chartExpensesCircle.ref.update({
      title: {
        text: '?????????????? ???? ???????????????????? ???? ' + getDaysName(days)
      },
      series: [{
        type: 'pie',
        data: res.expenseCircleChart.data as any
      }]
    })

    this.moneyLineChart.ref.update({
      xAxis: {
        categories: res.moneyLineChart.categories as any
      },
      title: {
        text: '?????????????????? ?????????????? ???? ' + getDaysName(days)
      },
      series: [
        {
          name: '????????????',
          type: 'spline',
          data: res.moneyLineChart.seriesData as any
        }
      ]
    })

    this.moneyLineChart.ref.reflow();
  }

// @ts-ignore

  chartIncomeLine = new Chart({
    chart: {
      type: 'line'
    },
    title: {
      text: '???????????? ?????????????? ???? 1 ??????????'
    },
    yAxis: {
      title: null
    },
    credits: {
      enabled: false
    },
    series: [
      {
        name: '???????????? ???? ??????????',
        type: 'spline',
        data: [1, 2, 3]
      }
    ]
  });

  // add point to chart serie
  chartIncomeCircle = new Chart({
    chart: {
      plotBorderWidth: null,
      plotShadow: false
    },
    title: {
      text: '???????????? ???? ???????????????????? ???? ??????????'
    },
    tooltip: {
      pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
      pie: {
        allowPointSelect: true,
        cursor: 'pointer',
        dataLabels: {
          enabled: true,
          format: '<b>{point.name}</b>: {point.percentage:.1f} %',
          style: {
            color:
              'black'
          }
        }
      }
    },
    series: [{
      type: 'pie',
      name: '????????',
      data: []
    }]
  });

  chartExpensesCircle = new Chart({
    chart: {
      plotBorderWidth: null,
      plotShadow: false
    },
    title: {
      text: '?????????????? ???? ???????????????????? ???? ??????????'
    },
    tooltip: {
      pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
      pie: {
        allowPointSelect: true,
        cursor: 'pointer',
        dataLabels: {
          enabled: true,
          format: '<b>{point.name}</b>: {point.percentage:.1f} %',
          style: {
            color:
              'black'
          }
        }
      }
    },
    series: [{
      type: 'pie',
      name: '????????',
      data: []
    }]
  });
  chartExpensesLine = new Chart({
    chart: {
      type: 'spline'
    },
    title: {
      text: '???????????? ???????????????? ???? 1 ??????????'
    },
    credits: {
      enabled: false
    },
    xAxis: {
      categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
    },
    series: [
      {
        name: '?????????????? ???? ??????????',
        type: 'spline',
        data: [1, 2, 3]
      }
    ]
  });
  moneyLineChart = new Chart({
    chart: {
      type: 'spline',
    },
    title: {
      text: '???????????? ?????????????????? ?????????????? ???? ??????????'
    },
    credits: {
      enabled: false
    },
    xAxis: {
      categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
    },
    series: [
      {
        name: '????????????',
        type: 'spline',
        data: [1, 2, 3]
      }
    ]
  });

  loadStatistics(number: number) {
    this.selectedDays = number;
    this.statisticsService.getAccountStatistic(this._selectedAccount.id, number).subscribe(res => {
      this.initCharts(res, number);
    });
  }
}
