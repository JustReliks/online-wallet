import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {MenuState} from "../card/card.component";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  @Output() menuState: EventEmitter<MenuState> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  changeMenuState(state: string) {
    this.menuState.emit(MenuState[state]);
  }
}
