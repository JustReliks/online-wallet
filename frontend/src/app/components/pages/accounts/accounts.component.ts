import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CreateAccountComponent} from "./create-account/create-account.component";
import {AuthUser} from "../../../entities/user";
import _ from "lodash";
import {AccountService} from "../../../service/account.service";
import {Account} from "../../../entities/account";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {
  private _currentUser: any;
  private _originalUser: any;
  private _accounts: Array<Account>;

  @Input('user') set user(user: AuthUser) {
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  get user() {
    return this._currentUser;
  }

  get accounts(): Array<Account> {
    return this._accounts;
  }

  constructor(private dialog: MatDialog,
              private _accountService: AccountService) {

  }

  ngOnInit(): void {
    this._accountService.getAccounts(this.user?.id).subscribe(res =>this._accounts=res);
  }

  createAccount() {
    console.log('create')
    const dialogRef = this.dialog.open(CreateAccountComponent, {
      width: '550px',
      data: {user: this.user}
    });
    dialogRef.afterClosed().subscribe(result => {
      this._accountService.getAccounts(this.user?.id).subscribe(res =>this._accounts=res);
    });
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }
}
