import {Component, OnInit} from '@angular/core';
import {Operation, OperationType} from "../../../entities/operation";
import {Account} from "../../../entities/account";
import {LoginComponent} from "../../common/login/login.component";
import {MatDialog} from "@angular/material/dialog";
import {SelectAccountComponent} from "../finance/select-account/select-account.component";
import {MatSelectChange} from "@angular/material/select";
import {MatOptionSelectionChange} from "@angular/material/core";

@Component({
  selector: 'app-income',
  templateUrl: './income.component.html',
  styleUrls: ['./income.component.scss']
})
export class IncomeComponent implements OnInit {

  private _accounts: Array<Account>;
  private _operations: Array<Operation>
  private _selectedAccount: Account;
  displayedColumns: string[] = ['id', 'date', 'value', 'currency', 'description']
  constructor(private dialog: MatDialog) {
    this._operations = new Array<Operation>();
    this._operations.push(new Operation(1, OperationType.INCOME, 15,"05.10.2005", "test", "RUB"));
    this._operations.push(new Operation(2, OperationType.INCOME, 31,"12.10.2005", "test", "RUB"));
    this._operations.push(new Operation(3, OperationType.INCOME, 23,"15.10.2005", "test", "RUB"));
    this._operations.push(new Operation(4, OperationType.INCOME, 24,"33.10.2005", "test", "RUB"));

    this._accounts = new Array<Account>();
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
}
