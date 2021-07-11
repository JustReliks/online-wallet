import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

import {Router} from '@angular/router';
import {AuthUser} from "../../../../entities/user";
import {AuthService} from "../../../../service/auth.service";
import {NotificationService} from "../../../../service/notification.service";
import {UserService} from "../../../../service/user.service";

@Component({
  selector: 'app-two-factor-modal',
  templateUrl: './two-factor-modal.component.html',
  styleUrls: ['./two-factor-modal.component.scss']
})
export class TwoFactorModalComponent implements OnInit {
  authMethod: any;
  private password: string;

  get authKey(): number {
    return this._authKey;
  }

  set authKey(value: number) {
    this._authKey = value;
  }

  private _authKey;

  get user() {
    return this._currentUser;
  }

  set user(value) {
    this._currentUser = value;
  }

  private _currentUser: AuthUser;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { user: AuthUser, password: string, loginComponent: any },
              private authService: AuthService,
              public dialogRef: MatDialogRef<TwoFactorModalComponent>,
              private notificationService: NotificationService,
              private router: Router,
              private userService: UserService,) {
    this.user = data.user;
    this.password = data.password;
    this.authMethod = data.loginComponent;
  }

  ngOnInit(): void {
  }

  googleAuthKeyKeyUp($event: any) {
    this._authKey = $event.target.value;
  }

  auth() {
    this.authService.loginTwoFactor(this.user.username, this.password, this.authKey).subscribe(res => {
      this.authMethod(res);
    }, error => {
      if (error.status === 403) {
        this.notificationService.showError('Неверный код. Повторите попытку.', 'Двухфакторная аутентификация');
        return;
      }
      this.notificationService.showError('Ошибка авторизации. Повторите попытку.', 'Авторизация');
    })
  }
}
