import {Component, Input, OnInit} from '@angular/core';
import {AuthUser} from "../../../../entities/user";
import _ from "lodash";
import {Account} from "../../../../entities/account";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {


  @Input() menuState: MenuState = MenuState.MENU;
  private _currentUser: any;
  private _originalUser: any;
  private _accounts: Array<Account>;

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

  constructor() {
  }

  ngOnInit(): void {
    console.log(MenuState.MENU.toString())
  }

  get accounts(): Array<Account> {
    return this._accounts;
  }

  changeMenuState($event: any) {
    console.log($event)
    this.menuState = $event;
  }

  setMenu() {
    this.menuState = MenuState.MENU;
  }

  getTitle(): string {
    if (this.menuState == MenuState.MENU) return 'Меню'
    else if (this.menuState == MenuState.INCOME) return 'Ваша прибыль'
    else if (this.menuState == MenuState.ACCOUNTS) return 'Ваши счета'
    else if (this.menuState == MenuState.TRANSACTIONS) return 'История транзакций по счетам'
    else if (this.menuState == MenuState.STATISTIC) return 'Статистика'

    return ''
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }
}

export enum MenuState {
  MENU = "menu", INCOME = "income", ACCOUNTS = "accounts", TRANSACTIONS = "transactions",
  STATISTIC = 'statistic',
}


