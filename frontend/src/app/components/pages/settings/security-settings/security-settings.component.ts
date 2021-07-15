import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthUser} from "../../../../entities/user";
import {UserService} from "../../../../service/user.service";
import {NotificationService} from "../../../../service/notification.service";
import _ from "lodash";
import {ConfirmedValidator} from "../../../common/registration/registration.component";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";

@Component({
  selector: 'app-security-settings',
  templateUrl: './security-settings.component.html',
  styleUrls: ['./security-settings.component.scss']
})
export class SecuritySettingsComponent implements OnInit {

  changePasswordGroup: FormGroup;
  imgBase64: any;
  private _currentUser: AuthUser;

  constructor(private _fb: FormBuilder,
              private userService: UserService,
              private notificationService: NotificationService) {
  }

  get user() {
    return this._currentUser;
  }

  @Input('user') set user(user: AuthUser) {
    if (user) {
      this._currentUser = _.cloneDeep(user);
      this.userService.generateGoogleAuthKey(this.user.username).subscribe(res => {
        this.imgBase64 = res;
      });
    }
  }

  get controls() {
    return this.changePasswordGroup.controls;
  }

  get form() {
    return this.changePasswordGroup;
  }

  private _validKey: boolean;

  get validKey(): boolean {
    return this._validKey;
  }

  set validKey(value: boolean) {
    this._validKey = value;
  }

  private _twoFactorToggleState: boolean;

  get twoFactorToggleState(): boolean {
    return this._twoFactorToggleState;
  }

  set twoFactorToggleState(value: boolean) {
    this._twoFactorToggleState = value;
  }

  private _twoFactorKey = '';

  get twoFactorKey(): string {
    return this._twoFactorKey;
  }

  set twoFactorKey(value: string) {
    this._twoFactorKey = value;
  }

  ngOnInit(): void {
    this.changePasswordGroup = this._fb.group({
        actualPassword: new FormControl('', [Validators.required]),
        password: new FormControl('', [Validators.required, Validators.minLength(7)]),
        confirmPassword: new FormControl(''),
        twoFactor: new FormControl('')
      },
      {
        validator: ConfirmedValidator('password', 'confirmPassword')
      }
    )
  }

  public hasControlsErrors = (controlName: string, errorName: string) => {
    return this.controls[controlName].hasError(errorName);
  };

  changePassword() {
    const oldPass = this.controls.actualPassword.value;
    const newPass = this.controls.password.value;
    const confirmNewPass = this.controls.confirmPassword.value;

    this.userService.changePassword(this.user.id, oldPass, newPass, confirmNewPass).subscribe(res => {
        localStorage.setItem('token', 'Bearer ' + res.token);
        this.notificationService.showSuccess('Пароль успешно изменен.', 'Смена пароля')
        this.changePasswordGroup.reset();
      }, error => {
        if (error.status === 403) {
          this.notificationService.showError('Неверный текущий пароль.', 'Смена пароля')
          return;
        }
        this.notificationService.showError('Возникла ошибка при смене пароля. Попробуйте позже.', 'Смена пароля')
      }
    );
  }

  onToggleChange($event: MatSlideToggleChange) {
    this.twoFactorToggleState = $event.checked;
    console.log($event)
  }

  checkCode(value: any) {
    this.userService.validateKey(this.user.username, value.target.value).subscribe(res => {
      this.validKey = res;
    });
  }

  changeTwoFactorState() {
    if (this.user.twoFactorEnabled) {
      this.changeTwoFactorStateRequest();
      return;
    }
    this.validateWithChange();
  }

  googleAuthKeyKeyUp($event: any) {
    this.twoFactorKey = $event.target.value;
  }

  regenerateQR() {
    this.userService.generateGoogleAuthKey(this.user.username, true).subscribe(res => {
      this.imgBase64 = res
      this.user.twoFactorEnabled = false;
      this.notificationService.showSuccess('QR код успешно перегенерирован.', 'Двухфакторной аутентификация')
    }, error => {
      this.notificationService.showError('Возникла ошибка. Повторите попытку позже.', 'Двухфакторной аутентификация')
    });
  }

  private validateWithChange() {
    this.userService.validateKey(this.user.username, this.twoFactorKey).subscribe(res => {
      if (!res) {
        this.notificationService.showError(
          'Вы ввели неверный код из Google Authenticator. Попробуйте еще раз.'
          , 'Двухфакторной аутентификация'
        )
        return;
      }
      this.changeTwoFactorStateRequest();
    });
  }

  private changeTwoFactorStateRequest() {
    this.userService
      .changeTwoFactorState(this.user.username, this.user.twoFactorEnabled ? 'off' : 'on')
      .subscribe((result) => {
        if (result) {
          this.notificationService.showSuccess(
            'Настройки двухфакторной аутентификации успешно изменены.'
            , 'Двухфакторной аутентификация'
          )
          AuthUser.updateFromUserLight(this.user, result)
          return;
        }
        this.notificationService.showError(
          'Возникла ошибка. Повторите попытку позже.'
          , 'Двухфакторной аутентификация'
        )
      }, error => {
        this.notificationService.showError(
          'Возникла ошибка. Повторите попытку позже.'
          , 'Двухфакторной аутентификация'
        )
      });
  }
}
