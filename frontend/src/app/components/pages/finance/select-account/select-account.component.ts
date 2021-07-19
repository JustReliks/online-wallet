import { Component, OnInit } from '@angular/core';
import {Account} from "../../../../entities/account";

@Component({
  selector: 'app-select-account',
  templateUrl: './select-account.component.html',
  styleUrls: ['./select-account.component.scss']
})
export class SelectAccountComponent implements OnInit {

  private _accounts: Array<Account>;
  private _selectedAccount: Account

  constructor() {

    // this._accounts = new Array<Account>();
    // this._accounts.push(new Account("test1", 15, "RUB"));
    // this._accounts.push(new Account("test2", 31, "RUB"));
    // this._accounts.push(new Account("test3", 45, "EUR"));


  }


  get accounts(): Array<Account> {
    return this._accounts;
  }

  set accounts(value: Array<Account>) {
    this._accounts = value;
  }

  ngOnInit(): void {

  }

  selectAccount(account: Account) {
    this._selectedAccount = account;
  }

  isAccountSelected() {
    return this._selectedAccount == null;
  }
}
