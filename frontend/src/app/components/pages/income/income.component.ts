import {Component, OnInit} from '@angular/core';
import {Operation, OperationType} from "../../../entities/operation";
import {Account} from "../../../entities/account";
import {LoginComponent} from "../../common/login/login.component";
import {MatDialog} from "@angular/material/dialog";
import {SelectAccountComponent} from "../finance/select-account/select-account.component";

@Component({
  selector: 'app-income',
  templateUrl: './income.component.html',
  styleUrls: ['./income.component.scss']
})
export class IncomeComponent implements OnInit {

  private _operations: Array<Operation>
  private _selectedAccount: Account;

  constructor(private dialog: MatDialog) {
    this._operations = new Array<Operation>();
    this._operations.push(new Operation(1, OperationType.INCOME, 15,"05.10.2005", "test", "RUB"));
    this._operations.push(new Operation(2, OperationType.INCOME, 31,"12.10.2005", "test", "RUB"));
    this._operations.push(new Operation(3, OperationType.INCOME, 23,"15.10.2005", "test", "RUB"));
    this._operations.push(new Operation(4, OperationType.INCOME, 24,"33.10.2005", "test", "RUB"));

  }

  selectAccount()
  {
    const dialogRef = this.dialog.open(SelectAccountComponent, {
      width: '340px',
      data: {}
    });
    dialogRef.afterClosed().subscribe(result => {
    });

  }

  isAccountSelected(): boolean {
    return this._selectedAccount == null;
  }


  get operations(): Array<Operation> {
    return this._operations;
  }

  set operations(value: Array<Operation>) {
    this._operations = value;
  }


  get chosenAccount(): Account {
    return this._selectedAccount;
  }

  set chosenAccount(value: Account) {
    this._selectedAccount = value;
  }

  ngOnInit(): void {
    console.log('Loaded');
  }

}
