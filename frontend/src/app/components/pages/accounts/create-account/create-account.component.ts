import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Goal} from "../../../../entities/goal";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Account} from "../../../../entities/account";
import {AccountBill} from "../../../../entities/account-bill";
import {Currency} from "../../../../entities/currency";
import {AuthUser} from "../../../../entities/user";
import {AccountService} from "../../../../service/account.service";
import {NotificationService} from "../../../../service/notification.service";
import {Icon} from "../../../../entities/icon";
import {DictionaryService} from "../../../../service/dictionary.service";

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent implements OnInit {

  private _goal: Goal;
  createAccountForm: FormGroup;
  private _account: Account;
  private _user: AuthUser;
  private _firstBill: AccountBill;
  private _secondBill: AccountBill;
  private _hasSecondBill: boolean = false;
  _icons: Icon[] = [];
  private _selectedIcon: Icon;
  minDate: Date;
  private _currencies: Array<Currency>;

  constructor(
    private dialogRef: MatDialogRef<any>,
    private _accountService: AccountService,
    private _notificationService: NotificationService, @Inject(MAT_DIALOG_DATA) public data: { user: AuthUser },
    private _dictionaryService: DictionaryService) {
    this._user = data.user;
    this.createAccountForm = new FormGroup({
      accountName: new FormControl('', [Validators.required]),
      mainCurrency: new FormControl('', [Validators.required]),
      description: new FormControl('', [Validators.required]),
      file: new FormControl(undefined, []),
      goalName: new FormControl('', [Validators.required]),
      goalValue: new FormControl('', [Validators.required]),
      goalDate: new FormControl(new Date().toDateString(), []),
    });
    this.minDate = new Date();
    // this.minDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDay() + 1);

    this._dictionaryService.getAllCurrencies().subscribe(res => this._currencies = res)

    this._icons = [];
    this._icons.push(new Icon('assets/img/accounts/1.png'))
    this._icons.push(new Icon('assets/img/accounts/2.png'))
    this._icons.push(new Icon('assets/img/accounts/3.png'))
    this._icons.push(new Icon('assets/img/accounts/4.png'))

  }

  get currencies(): Array<Currency> {
    return this._currencies;
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

  get hasSecondBill(): boolean {
    return this._hasSecondBill;
  }

  set hasSecondBill(value: boolean) {
    this._hasSecondBill = value;
  }

  get firstBill(): AccountBill {
    return this._firstBill;
  }

  set firstBill(value: AccountBill) {
    this._firstBill = value;
  }

  get secondBill(): AccountBill {
    return this._secondBill;
  }

  set secondBill(value: AccountBill) {
    this._secondBill = value;
  }

  get account(): Account {
    return this._account;
  }

  set account(value: Account) {
    this._account = value;
  }

  get controls() {
    return this.createAccountForm.controls;
  }

  ngOnInit(): void {
  }

  changeMainCurrency() {

  }

  close() {
    this.dialogRef.close();
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

  createAccount() {

  }

  create() {
    this._account = new Account({});
    console.log(this.controls)
    this._account.name = this.controls.accountName.value;
    this._account.description = this.controls.description.value;
    let accountBill = new AccountBill({
      currency: this.getCurrency(this.controls.mainCurrency.value)
    });
    this._account.accountBills = new Array<AccountBill>(accountBill);
    this._account.userId = this._user.id;
    if(this.goal != null) {
      this.goal.value = this.controls.goalValue.value;
      this.goal.name = this.controls.goalName.value;
      this.goal.date = this.controls.goalDate.value;
      this._account.goal = this.goal;
    }
    console.log(this._account)
    this._accountService.createAccount(this.account).subscribe(res => {
      this._notificationService.showSuccess('Новый счет успешно создан', 'Финанасы')
      this.dialogRef.close();
    }, error => {
      this._notificationService.showError('Возникла ошибка. Повторите попытку позже.', 'Финанасы')
    })
  }

  private getCurrency(currId: number) {
    return this.currencies?.find(curr => curr.id == currId);
  }

  hasControlsErrors(controlName: string, errorName: string) {
    return this.controls[controlName].hasError(errorName);
  }
}
