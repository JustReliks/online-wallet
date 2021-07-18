import { Component, OnInit } from '@angular/core';
import {LoginComponent} from "../../common/login/login.component";
import {MatDialog} from "@angular/material/dialog";
import {CreateAccountComponent} from "./create-account/create-account.component";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  constructor(private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  createAccount() {
    console.log('create')
    const dialogRef = this.dialog.open(CreateAccountComponent, {
      width: '550px',
      data: {}
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
