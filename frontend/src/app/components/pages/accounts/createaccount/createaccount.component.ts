import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-createaccount',
  templateUrl: './createaccount.component.html',
  styleUrls: ['./createaccount.component.scss']
})
export class CreateAccountComponent implements OnInit {

  accountNameFilled: boolean

  constructor() { }

  ngOnInit(): void {
  }

  changeMainCurrency() {

  }
}
