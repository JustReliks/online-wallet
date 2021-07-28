import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
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
import _ from "lodash";
import {Type} from "../../../../entities/type";
import {DomSanitizer} from "@angular/platform-browser";
import {AccountType} from "../../../../entities/account-type";

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
  private _selectedTypeId: number;
  minDate: Date;
  minDateCredit: Date;
  private _currencies: Array<Currency>;
  private _types: Array<Type>;
  private _accountTypeInfo: boolean = false;
  private typesMap: any;

  constructor(
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<any>,
    private _accountService: AccountService,
    private _notificationService: NotificationService, @Inject(MAT_DIALOG_DATA) public data: { user: AuthUser },
    private _dictionaryService: DictionaryService,
    private _sanitizer: DomSanitizer) {
    this._user = data.user;
    let date = new Date();
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate() + 2);
    this.minDateCredit = new Date();
    this.minDateCredit.setDate(this.minDateCredit.getDate() + 90);
    console.log(date.getDay())
    console.log(date.getDate())
    this.createAccountForm = new FormGroup({
      accountName: new FormControl('', [Validators.required]),
      mainCurrency: new FormControl('', [Validators.required]),
      additionalCurrency: new FormControl('', []),
      description: new FormControl('', [Validators.required]),
      goalName: new FormControl('Моя первая цель', []),
      goalValue: new FormControl('10000', []),
      goalDate: new FormControl(this.minDate, []),
      freezeDate: new FormControl(this.minDate),
      creditAmount: new FormControl('', []), file: new FormControl(undefined, []),

      creditRate: new FormControl('', []),
      creditTo: new FormControl(this.minDateCredit),
    });

    this._dictionaryService.getAllCurrencies().subscribe(res => this._currencies = res)
    this._dictionaryService.getAllAccountTypes().subscribe(res => {
      this.types = res

      this.typesMap = _.chain(res)
        .keyBy('id')
        .mapValues('code')
        .value();
    });

    this._icons = [];
    this._icons.push(new Icon('assets/img/accounts/1.png'))
    this._icons.push(new Icon('assets/img/accounts/2.png'))
    this._icons.push(new Icon('assets/img/accounts/3.png'))
    this._icons.push(new Icon('assets/img/accounts/4.png'))

    _accountService.updateTypeInfoObservable.subscribe(value => {
      this._accountTypeInfo = false
    });

  }

  set types(value: Array<Type>) {
    this._types = value;
  }

  get types(): Array<Type> {
    return this._types;
  }

  get currencies(): Array<Currency> {
    return this._currencies;
  }

  get selectedTypeId(): number {
    return this._selectedTypeId;
  }

  set selectedTypeId(value: number) {
    if (value == this.selectedTypeId) {
      this._selectedTypeId = null;
    } else {
      this._selectedTypeId = value;
      let typesMapElement = this.typesMap[value];
      if (typesMapElement != 'CUMULATIVE' && typesMapElement != 'SAVING') {
        this.goal = null;
      }

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
    let findType = _.find(this.types, type => type.id == this.selectedTypeId);
    let accountBills = new Array<AccountBill>();
    let isCreditBill = findType.code == 'CREDIT';
    if (isCreditBill) {
      let value = this.controls.creditRate.value;
      if (value < 0) {
        this._notificationService.showError('Процент по кредиту не может быть меньше 0.', 'Финанасы')
        return;
      }
    }
    accountBills.push(new AccountBill({
      currency: this.getCurrency(this.controls.mainCurrency.value),
      rate: isCreditBill ? this.controls.creditRate.value : 0,
      balance: isCreditBill ? this.controls.creditAmount.value : 0,
      maturityDate: isCreditBill ? this.controls.creditTo.value : null
    }));
    if (this.controls.additionalCurrency.value) {
      accountBills.push(new AccountBill({
        currency: this.getCurrency(this.controls.additionalCurrency.value)
      }))
    }
    this._account.accountBills = accountBills;
    this._account.userId = this._user.id;
    if (this.goal != null) {
      this.goal.value = this.controls.goalValue.value;
      this.goal.name = this.controls.goalName.value;
      this.goal.date = this.controls.goalDate.value;
      this._account.goal = this.goal;
    }

    this._account.accountType = new AccountType({
      type: findType
    })

    if (this.selectedTypeId == 1) {
      this._account.freezeDate = this.controls.freezeDate?.value;
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

  getCurrenciesWithoutMain() {
    return _.filter(this.currencies, curr => curr.id != this.controls.mainCurrency.value)
  }

  getTypeIcon(type: Type) {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${type?.icon}`);
  }


  get accountTypeInfo(): boolean {
    return this._accountTypeInfo;
  }

  changeAccountTypeInfo() {
    this._accountTypeInfo = !this._accountTypeInfo;
  }

  getSelectedTypeCode(selectedTypeId: number) {
    if (selectedTypeId) {
      return this.typesMap[selectedTypeId]
    }
  }
}
