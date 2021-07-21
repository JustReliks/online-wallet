import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AuthUser} from "../../../../entities/user";
import {Account} from "../../../../entities/account";
import {AccountService} from "../../../../service/account.service";
import {AccountBill} from "../../../../entities/account-bill";
import {NotificationService} from "../../../../service/notification.service";

@Component({
  selector: 'app-add-transaction-modal',
  templateUrl: './add-transaction-modal.component.html',
  styleUrls: ['./add-transaction-modal.component.scss']
})
export class AddTransactionModalComponent implements OnInit {

  addTransactionForm: FormGroup;
  private _user: AuthUser;
  private _accounts: Array<Account>;
  private _selectedAccount: Account;
  selectedBill: AccountBill;
  isPlusState: boolean = true;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { user: AuthUser, accounts: Array<Account> },
              private _accountService: AccountService,
              private _notification: NotificationService,
              private dialogRef: MatDialogRef<any>) {
    this._user = data.user;
    this._accounts = data.accounts;

    this.addTransactionForm = new FormGroup({
      account: new FormControl('', [Validators.required]),
      sum: new FormControl('', [Validators.min(1)]),
    })
  }

  get selectedAccount(): Account {
    return this._selectedAccount;
  }

  get user(): AuthUser {
    return this._user;
  }

  get accounts(): Array<Account> {
    return this._accounts;
  }

  ngOnInit(): void {
  }

  close() {

  }

  add() {
    let value = this.addTransactionForm.controls.sum.value;
    this._accountService.addTransaction(this.selectedBill, this.user.id, value, this.isPlusState).subscribe(res => {
      this.selectedBill = res;
      this._notification.showSuccess('Операция успешно проведена.', 'Проведение операции по счету')
      this.dialogRef.close();
      console.log(res)
    }, error => {
      this._notification.showError('Возникла ошибка при проведении операции: ' + error.error.message, 'Проведение операции по счету')
    });
  }

  selectAccount(account: Account) {
    this._selectedAccount = account;
  }

  selectBill(accountBill: AccountBill) {
    console.log(accountBill);
    this.selectedBill = accountBill;
  }
}
