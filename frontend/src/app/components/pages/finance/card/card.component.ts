import {Component, Input, OnInit} from '@angular/core';
import {AuthUser} from "../../../../entities/user";
import _ from "lodash";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {

  @Input() menuState: MenuState = MenuState.MENU;
  private _currentUser: any;
  private _originalUser: any;

  @Input('user') set user(user: AuthUser) {
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  get user() {
    return this._currentUser;
  }

  constructor() {
  }

  ngOnInit(): void {
    console.log(MenuState.MENU.toString())
  }

  changeMenuState($event: any) {
    console.log($event)
    this.menuState = $event;
  }

  getTitle(): string {
    if (this.menuState == MenuState.MENU) return 'Меню'
    else if (this.menuState == MenuState.INCOME) return 'Ваша прибыль'
    else if (this.menuState == MenuState.ACCOUNTS) return 'Ваши счета'
    else if (this.menuState == MenuState.TRANSACTIONS) return 'История транзакций по счетам'

    return ''
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }
}

export enum MenuState {
  MENU = "menu", INCOME = "income", ACCOUNTS = "accounts", TRANSACTIONS = "transactions"
}


