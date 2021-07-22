import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {TwoFactorModalComponent} from './two-factor-modal/two-factor-modal.component';
import {filter} from 'rxjs/operators';
import {AuthService} from "../../../service/auth.service";
import {NotificationService} from "../../../service/notification.service";
import {AuthUser} from "../../../entities/user";
import {RestoreAccessComponent} from "../../pages/restore-access/restore-access.component";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup;

  constructor(private router: Router,
              private authService: AuthService,
              public dialogRef: MatDialogRef<LoginComponent>,
              private notificationService: NotificationService,
              private dialog: MatDialog) {
  }

  get controls() {
    return this.loginForm.controls;
  }

  get form() {
    return this.loginForm;
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });
  }

  onSignIn(signBtn: MatButton): void {
    this.disableBtnOnTime(signBtn, 3000);
    if (this.form.valid) {
      this.authService.login(this.controls.username.value, this.controls.password.value).pipe(filter(data => data != null))
        .subscribe(data => {
          if (data.twoFactorEnabled) {
            this.openTwoFactorModal(data, this.controls.password.value);
            this.dialogRef.close();
          } else {
            this.auth(data);
          }
        }, error => {
          if (error.status === 401) {
            this.router.navigate(['']);
            this.notificationService.showError('Неверный логин или пароль.', 'Авторизация');
            return;
          }
          this.router.navigate(['']);
          this.notificationService.showError('Ошибка авторизации. Повторите попытку.', 'Авторизация');
          // this.form.setErrors({authorizedError: true});
        });
    }
  }

  public auth(data: AuthUser) {
    localStorage.setItem('username', data.username);
    localStorage.setItem('token', 'Bearer ' + data.token);
    this.authService.setCurrentUser(data);

    this.dialogRef.close();
    this.notificationService.showSuccess('Вы успешно вошли в аккаунт', 'Авторизация');
    const page = localStorage.getItem('requestedAuthPage');
    if (page) {
      this.router.navigate([page]);
    }
  }

  private disableBtnOnTime(saveBtn: MatButton, ms: number) {
    saveBtn.disabled = true;
    setTimeout(() => (saveBtn.disabled = false), ms);
  }

  close(): void {
    this.dialogRef.close();
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.controls[controlName].hasError(errorName);
  }

  public hasControlsErrors = (controlName: string, errorName: string) => {
    return this.controls[controlName].hasError(errorName);
  };

  openTwoFactorModal(user: AuthUser, password: string) {
    const dialogRef = this.dialog.open(TwoFactorModalComponent, {
      width: '551px',
      // maxHeight: '95vh',
      data: {
        user,
        password,
        loginComponent: this.auth
      },
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }

  openRestoreAccessModal() {
    this.dialogRef.close();
    const dialogRef = this.dialog.open(RestoreAccessComponent, {
      width: '610px',
       maxHeight: '95vh',
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
