import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../service/auth.service";
import {filter, take} from "rxjs/operators";
import {AuthUser} from "../../../entities/user";
import _ from "lodash";
import {AccountService} from "../../../service/account.service";
import {Account} from "../../../entities/account";
import {MatDialog} from "@angular/material/dialog";
import {AddTransactionModalComponent} from "./add-transaction-modal/add-transaction-modal.component";
import {DictionaryService} from "../../../service/dictionary.service";
import {Currency} from "../../../entities/currency";

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.scss']
})
export class FinanceComponent implements OnInit {
  private _originalUser: AuthUser;
  private _currentUser: AuthUser;
  private _accounts: Array<Account>;
  currencyToConvert: any = null;
  private _balance: any;
  private _currencies: Array<Currency>;

  constructor(private _authService: AuthService,
              private _accountService: AccountService,
              private _dictionaryService: DictionaryService,
              private dialog: MatDialog) {
    this._authService.getCurrentLoggedUser().pipe(filter(res => res != null), take(1)).subscribe(res => {
      this.user = res;
      this._accountService.getBalance(this.user.id, this.currencyToConvert).subscribe(res => this._balance = res);
      this._accountService.getAccounts(this.user.id).subscribe(res => this.accounts = res);
      this._dictionaryService.getAllCurrencies().subscribe(res => this.currencies = res)
    });

    this._accountService.updateAccountsSubjectObservable.subscribe(res => this.accounts = res.accounts);
  }

  get currencies(): Array<Currency> {
    return this._currencies;
  }

  set currencies(value: Array<Currency>) {
    this._currencies = value;
  }

  get balance(): any {
    return this._balance;
  }

  get accounts(): Array<Account> {
    return _.sortBy(this._accounts, 'name');
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
      this._accountService.getAccounts(this.user?.id).subscribe(res => {
        this._accounts = res
        this._accountService.updateAccountsEvent({
          accounts: this.accounts
        })
      });
      this._accountService.getBalance(this.user.id, this.balance.currency).subscribe(res => this._balance = res);
    });
  }

  changeCurrency() {
    let start = _.findIndex(this.currencies, curr => curr.shortName == this.balance.currency);
    console.log(start, this.currencies.length)
    this.currencyToConvert = start + 1 == this.currencies.length
      ? this.currencies[0].shortName
      : this.currencyToConvert = _.slice(this.currencies, start + 1, start + 2 > this.currencies.length ? -1 : start + 2)[0].shortName;
    this._accountService.getBalance(this.user.id, this.currencyToConvert).subscribe(res => this._balance = res);
    console.log(this.currencyToConvert)
  }
}
