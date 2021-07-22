import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Account} from "../../../../entities/account";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DictionaryService} from "../../../../service/dictionary.service";
import {Currency} from "../../../../entities/currency";
import {Icon} from "../../../../entities/icon";
import {Goal} from "../../../../entities/goal";
import {AccountBill} from "../../../../entities/account-bill";
import {AccountService} from "../../../../service/account.service";

@Component({
  selector: 'app-account-settings',
  templateUrl: './account-settings.component.html',
  styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {

  _account: Account;
  public editAccountForm: FormGroup;
  private _currencies: Array<Currency>;
  hasSecondBill: boolean;
  _icons: Icon[] = [];
  private _selectedIcon: Icon;
  private _goal: Goal;
  minDate: Date;
  private _secondBill: AccountBill;


  constructor(private accountService: AccountService,
              private dialogRef: MatDialogRef<any>,
              private dictionary: DictionaryService,
              @Inject(MAT_DIALOG_DATA) public data: { account: Account }) {
    this._account = data.account;
    this.editAccountForm = new FormGroup({
      accountName: new FormControl(this._account.name, [Validators.required]),
      description: new FormControl(this._account.description, [Validators.required]),
      goalName: new FormControl(this._account.goal?.name, []),
      goalValue: new FormControl(this._account.goal?.value, []),
      goalDate: new FormControl(this._account.goal?.date, []),
    });
    dictionary.getAllCurrencies().subscribe(value => this._currencies = value);
    this.hasSecondBill = this.account.accountBills.length == 2;
    if (this.hasSecondBill) this.secondBill = this.account.accountBills[1];
    this._icons = [];
    this._icons.push(new Icon('assets/img/accounts/1.png'))
    this._icons.push(new Icon('assets/img/accounts/2.png'))
    this._icons.push(new Icon('assets/img/accounts/3.png'))
    this._icons.push(new Icon('assets/img/accounts/4.png'))

    this.minDate = new Date();
    this.goal = this.account.goal;
  }


  set secondBill(value: AccountBill) {
    this._secondBill = value;
  }

  close() {
    this.dialogRef.close();
  }


  get selectedIcon(): Icon {
    return this._selectedIcon;
  }

  set selectedIcon(value: Icon) {
    if (value == this.selectedIcon) {
      this._selectedIcon = null;
    } else {
      this._selectedIcon = value;
    }
  }

  get icons(): Icon[] {
    return this._icons;
  }

  createGoal() {
    this._goal = new Goal({});
  }


  get goal(): Goal {
    return this._goal;
  }

  set goal(value: Goal) {
    this._goal = value;
  }


  get account(): Account {
    return this._account;
  }

  get currencies(): Array<Currency> {
    return this._currencies;
  }

  ngOnInit(): void {
  }

  hasControlsErrors(controlName: string, errorName: string) {
    return this.controls[controlName].hasError(errorName);
  }

  get controls() {
    return this.editAccountForm.controls;
  }


  edit() {
    this.account.name = this.controls.accountName.value;
    this.account.description = this.controls.description.value;
    if (this.goal != null) {
      this.goal.name = this.controls.goalName.value;
      this.goal.value = this.controls.goalValue.value;
      this.goal.date = this.controls.goalDate.value;
    }
    this.account.goal = this.goal;
    this.accountService.updateAccount(this.account).subscribe(res => console.log(res));
    close();
  }

  delete() {
    this.accountService.deleteAccount(this.account).subscribe(res => console.log(res));
    close();
  }
}
