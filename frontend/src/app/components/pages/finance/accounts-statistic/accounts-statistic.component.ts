import {Component, Input, OnInit} from '@angular/core';
import {AuthUser} from "../../../../entities/user";
import _ from "lodash";
import {MatDialog} from "@angular/material/dialog";
import {Operation} from "../../../../entities/operation";
import {Account} from "../../../../entities/account";
import {DomSanitizer} from "@angular/platform-browser";
import {Chart} from 'angular-highcharts';

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

  @Input('user') set user(user: AuthUser) {
    console.log(user)
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  @Input('accounts') set accounts(accounts: Array<Account>) {
    this._accounts = accounts;
  }


  get user() {
    return this._currentUser;
  }

  displayedColumns: string[] = ['id', 'date', 'value', 'currency', 'description']
  accountsExpanded: boolean = true;

  constructor(private dialog: MatDialog,
              private _sanitizer: DomSanitizer) {
    this._accounts = new Array<Account>();
    // this._accounts.push(new Account("test1", 15, "RUB"));
    // this._accounts.push(new Account("test2", 31, "RUB"));
    // this._accounts.push(new Account("test3", 45, "EUR"));
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
    console.log('Loaded');
  }

  selectAccount($event: any) {
    this._selectedAccount = $event;
    console.log(this._selectedAccount.name);
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }

  getAccountImg(account: Account) {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${account.accountType.type?.icon}`);
  }

  showAccountStatistic(account: Account) {
    this.accountsExpanded = false;
  }

  // @ts-ignore

  chart = new Chart({
    chart: {
      type: 'line'
    },
    title: {
      text: 'Linechart'
    },
    credits: {
      enabled: false
    },
    series: [
      {
        name: 'Line 1',
        type: 'line',
        data: [1, 2, 3]
      }
    ]
  });

  // add point to chart serie
  add() {
    this.chart.addPoint(Math.floor(Math.random() * 10));
  }
}
