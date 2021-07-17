import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {

  @Input()  menuState: MenuState = MenuState.ACCOUNTS;

  constructor() { }

  ngOnInit(): void {
    console.log(MenuState.MENU.toString())
  }

  changeMenuState($event: any) {
    console.log($event)
    this.menuState = $event;
  }

  getTitle(): string {
    if(this.menuState == MenuState.MENU) return 'Меню'
    else if(this.menuState == MenuState.INCOME) return 'Ваша прибыль'
    else if(this.menuState == MenuState.ACCOUNTS) return 'Ваши счета'

    return ''
  }

}

export enum MenuState  {
  MENU = "menu", INCOME = "income", ACCOUNTS = "accounts"
}


