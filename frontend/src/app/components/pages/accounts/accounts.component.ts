import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CreateAccountComponent} from "./create-account/create-account.component";
import {AuthUser} from "../../../entities/user";
import _ from "lodash";
import {AccountService} from "../../../service/account.service";
import {Account} from "../../../entities/account";
import {AccountSettingsComponent} from "./account-settings/account-settings.component";
import {Type} from "../../../entities/type";
import {DomSanitizer} from "@angular/platform-browser";

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

  set accounts(accounts: Array<Account>) {
    this._accounts = _.sortBy(accounts, 'id');
  }

  get accounts(): Array<Account> {
    return this._accounts;
  }

  constructor(private dialog: MatDialog,
              private _accountService: AccountService,
              private _sanitizer: DomSanitizer) {
    _accountService.updateAccountsSubjectObservable.subscribe(res => this.accounts = res.accounts);

  }

  ngOnInit(): void {
    this._accountService.getAccounts(this.user?.id).subscribe(res => this.accounts = res);
  }

  createAccount() {
    console.log('create')
    const dialogRef = this.dialog.open(CreateAccountComponent, {
      width: '550px',
      data: {user: this.user}
    });
    dialogRef.afterClosed().subscribe(result => {
      this._accountService.getAccounts(this.user?.id).subscribe(res => {
        this.accounts = res
        this._accountService.updateAccountsEvent({
          accounts: this.accounts
        })
      });
    });
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }

  getGoalProgressBarValue(account: Account) {
    if(account.goal.completed) return 100;
    let percent = account.convertedBalance.balance * 100 / account.goal.value;
    return percent > 100 ? 100 : percent
  }

  isGoalExpired(account: Account): boolean {
    if (account.goal != null && account.convertedBalance.balance < account.goal.value) {
      return Date.now() > Date.parse(account.goal.date);
    }
    return false;
  }

  openSettings(account: Account) {
    const dialogRef = this.dialog.open(AccountSettingsComponent, {
      width: '550px',
      data: {account: account}
    });
    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.accounts = _.remove(this._accounts, acc => acc.id != res)
        this._accountService.updateAccountsEvent({
          accounts: this.accounts
        });
      }
    })
  }

  getTypeIcon(type: Type) {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${type?.icon}`);
  }
}
