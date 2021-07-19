import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Account} from "../../../../entities/account";
import {AccountBill} from "../../../../entities/account-bill";
import {Currency} from "../../../../entities/currency";
import {AuthUser} from "../../../../entities/user";
import {AccountService} from "../../../../service/account.service";
import {NotificationService} from "../../../../service/notification.service";

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent implements OnInit {

  accountNameFilled: boolean
   createAccountForm: FormGroup;
  private _account: Account;
  private _user: AuthUser;

  constructor(private dialogRef: MatDialogRef<any>,
              private _accountService:AccountService,
              private _notificationService:NotificationService) {
    this.createAccountForm = new FormGroup({
      accountName: new FormControl('', [Validators.required]),
      mainCurrency: new FormControl('', [Validators.required]),
      description: new FormControl('', [Validators.required]),
      file: new FormControl(undefined, []),
    });
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

  create() {
    this._account.name = this.controls.accountName.value;
    let currency = new Currency({
      _shortName: this.controls.mainCurrency.value
    });
    let accountBill = new AccountBill({
      _currency: currency
    });
    this._account.accountBills = new Array<AccountBill>(accountBill);
    this._account.userId = this._user.id;

    this._accountService.createAccount(this.account).subscribe(res => {
      this._notificationService.showSuccess('Настройки успешно изменены.', 'Настройки аккаунта')
    }, error => {
      this._notificationService.showError('Возникла ошибка. Повторите попытку позже.', 'Настройки аккаунта')
    })
  }

  hasControlsErrors(controlName: string, errorName: string) {
    return this.controls[controlName].hasError(errorName);
  }
}
