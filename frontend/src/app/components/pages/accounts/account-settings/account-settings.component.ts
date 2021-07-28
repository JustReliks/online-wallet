import {ChangeDetectionStrategy, Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Account} from "../../../../entities/account";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DictionaryService} from "../../../../service/dictionary.service";
import {Icon} from "../../../../entities/icon";
import {Goal} from "../../../../entities/goal";
import {AccountBill} from "../../../../entities/account-bill";
import {AccountService} from "../../../../service/account.service";
import {NotificationService} from "../../../../service/notification.service";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-account-settings',
  templateUrl: './account-settings.component.html',
  styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {

  _account: Account;
  public editAccountForm: FormGroup;
  hasSecondBill: boolean;
  private _selectedIcon: Icon;
  private _goal: Goal;
  minDate: Date;
  private _secondBill: AccountBill;
  _isGoalValid: boolean = true;


  constructor(private accountService: AccountService,
              private dialogRef: MatDialogRef<any>,
              private dictionary: DictionaryService,
              @Inject(MAT_DIALOG_DATA) public data: { account: Account },
              private notificationService: NotificationService) {
    this._account = data.account;
    this.editAccountForm = new FormGroup({
      accountName: new FormControl(this._account.name, [Validators.required]),
      description: new FormControl(this._account.description, [Validators.required]),
      goalName: new FormControl(this._account.goal?.name, []),
      goalValue: new FormControl(this._account.goal?.value, []),
      goalDate: new FormControl(this._account.goal?.date, []),
    });
    this.hasSecondBill = this.account.accountBills.length == 2;
    if (this.hasSecondBill) this.secondBill = this.account.accountBills[1];

    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate() + 2);
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

  createGoal() {
    this._goal = new Goal({});
    this._isGoalValid = false;
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

  ngOnInit(): void {
    this._isGoalValid = true;
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

      this.goal.completed = this.goal.value <= this.account.convertedBalance.balance;
    }
    this.account.goal = this.goal;
    this.accountService.updateAccount(this.account).subscribe(res => {
      this._account = res;
      this.notificationService.showSuccess("Информация по счёту успешно обновлена.", "Редактирование счёта");
      this.dialogRef.close();
    }, error => {
      this.notificationService.showError('Возникла ошибка. Повторите попытку позже.', "Редактирование счёта")
    });
  }

  changeGoalValue(withoutDate?: any) {
    this._isGoalValid = this.controls.goalName.value?.length != 0 && this.controls.goalValue.value?.length != 0 && this.controls.goalDate.value != null;
  }

  delete() {
    this.accountService.deleteAccount(this.account).subscribe(res => {
      this.notificationService.showSuccess("Счёт успешно удален.", "Редактирование счёта");
      this.dialogRef.close(this.account.id);
    }, error => {
      this.notificationService.showError('Возникла ошибка. Повторите попытку позже.', "Редактирование счёта")
    });
  }
}
