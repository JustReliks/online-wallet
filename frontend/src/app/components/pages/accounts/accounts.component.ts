import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CreateAccountComponent} from "./create-account/create-account.component";
import {AuthUser} from "../../../entities/user";
import _ from "lodash";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  private _currentUser: any;
  private _originalUser: any;

  @Input('user') set user(user: AuthUser) {
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  get user() {
    return this._currentUser;
  }

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  createAccount() {
    console.log('create')
    const dialogRef = this.dialog.open(CreateAccountComponent, {
      width: '550px',
      data: {user: this.user}
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }
}
