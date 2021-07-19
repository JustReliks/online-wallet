import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../service/auth.service";
import {filter, take} from "rxjs/operators";
import {AuthUser} from "../../../entities/user";
import _ from "lodash";
import {AccountService} from "../../../service/account.service";
import {Account} from "../../../entities/account";
import {MatDialog} from "@angular/material/dialog";
import {AddTransactionModalComponent} from "./add-transaction-modal/add-transaction-modal.component";

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.scss']
})
export class FinanceComponent implements OnInit {

  private _originalUser: AuthUser;
  private _currentUser: AuthUser;
  private _accounts: Array<Account>;

  constructor(private _authService: AuthService,
              private _accountService: AccountService,
              private dialog: MatDialog) {
    this._authService.getCurrentLoggedUser().pipe(filter(res => res != null), take(1)).subscribe(res => {
      this.user = res;

      this._accountService.getAccounts(this.user.id).subscribe(res => this.accounts = res);
    });
  }

  get accounts(): Array<Account> {
    return _.sortBy(this._accounts,'name');
  }

  set accounts(value: Array<Account>) {
    this._accounts = value;
  }

  get user(): AuthUser {
    return this._currentUser;
  }

  set user(user: AuthUser) {
    this._originalUser = user;
    console.log(this._originalUser)
    this.cloneOriginalUser();
  }

  ngOnInit(): void {
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }

  addTransaction() {
    const dialogRef = this.dialog.open(AddTransactionModalComponent, {
      width: '550px',
      data: {user: this.user, accounts: this.accounts}
    });
    dialogRef.afterClosed().subscribe(result => {
      this._accountService.getAccounts(this.user?.id).subscribe(res => this._accounts = res);
    });
  }
}
