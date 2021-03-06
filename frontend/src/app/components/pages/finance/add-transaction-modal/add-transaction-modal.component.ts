import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AuthUser} from "../../../../entities/user";
import {Account} from "../../../../entities/account";
import {AccountService} from "../../../../service/account.service";
import {AccountBill} from "../../../../entities/account-bill";
import {NotificationService} from "../../../../service/notification.service";
import {DictionaryService} from "../../../../service/dictionary.service";
import {CategoryType, TransactionCategory} from "../../../../entities/transaction-category";
import {DomSanitizer} from "@angular/platform-browser";

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
  private categories: Array<TransactionCategory>;
  selectedCategoryId: number;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { user: AuthUser, accounts: Array<Account> },
              private _accountService: AccountService,
              private _notification: NotificationService,
              private dialogRef: MatDialogRef<any>,
              private _dictionaryService: DictionaryService,
              private _sanitizer: DomSanitizer) {
    this._user = data.user;
    this._accounts = data.accounts;

    this.addTransactionForm = new FormGroup({
      account: new FormControl('', [Validators.required]),
      sum: new FormControl('', [Validators.required, Validators.min(1)]),
    })
    this._dictionaryService.getAllTransactionCategories().subscribe(res => this.categories = res);
  }

  changePlusState(): void {
    let accountCode = this.selectedAccount?.accountType?.type.code;
    if (accountCode != 'CREDIT') {
      if (accountCode != 'SAVING' || Date.now() > Date.parse(this.selectedAccount.freezeDate)) {
        this.isPlusState = !this.isPlusState;
        this.selectedCategoryId = null;
      }
    }
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
    this.dialogRef.close();
  }

  add() {
    let value = this.addTransactionForm.controls.sum.value;
    this._accountService.addTransaction(this.selectedBill, this.user.id, value, this.isPlusState, this.selectedCategoryId).subscribe(res => {
      this.selectedBill = res;
      this._notification.showSuccess('???????????????? ?????????????? ??????????????????.', '???????????????????? ???????????????? ???? ??????????')
      this.dialogRef.close();
    }, error => {
      if (error.status === 400) {
        this._notification.showError('???????????????? ???????????? ?????? ???????????????????? ????????????????: ???????????? ???????????????? ???????????????? ?? ???????????????????????????? ??????????????. ???????????????? ????????????????.', '???????????????????? ???????????????? ???? ??????????')
      }
    });
  }

  selectAccount(account: Account) {
    this._selectedAccount = account;
  }

  selectBill(accountBill: AccountBill) {
    this.selectedBill = accountBill;
  }

  getCategories(): Array<TransactionCategory> {
    return this.categories.filter(category => this.isPlusState ? category.type == CategoryType.INCOME : category.type == CategoryType.EXPENSES);
  }

  getCategoryImg(category: TransactionCategory) {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${category?.icon}`);
  }

  isSelectedAccountFrozen() {
    return (this.selectedAccount?.accountType?.type?.code == 'SAVING') && (Date.now() < Date.parse(this.selectedAccount.freezeDate));

  }
}

