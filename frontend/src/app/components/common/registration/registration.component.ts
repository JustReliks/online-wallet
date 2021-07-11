import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {LoginComponent} from '../login/login.component';
import {AuthService} from "../../../service/auth.service";
import {UserService} from "../../../service/user.service";
import {NotificationService} from "../../../service/notification.service";

export function ConfirmedValidator(
  controlName: string,
  matchingControlName: string
) {
  return (formGroup: FormGroup) => {
    const control = formGroup.controls[controlName];
    const matchingControl = formGroup.controls[matchingControlName];
    if (matchingControl.errors && !matchingControl.errors.confirmedValidator) {
      return;
    }
    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({confirmedValidator: true});
    } else {
      matchingControl.setErrors(null);
    }
  };
}

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  registerForm: FormGroup;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<LoginComponent>,
    private authService: AuthService,
    private notificationService: NotificationService
  ) {
  }

  get controls() {
    return this.registerForm.controls;
  }

  get form() {
    return this.registerForm;
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group(
      {
        username: new FormControl('', [
          Validators.required,
          this.noWhiteSpaceValidator,
          Validators.pattern('^[A-Za-z0-9_]*$'),
          Validators.minLength(3),
          Validators.maxLength(17)
        ]),
        email: new FormControl('', [Validators.required, Validators.email]),
        password: new FormControl('', [Validators.required, Validators.minLength(7)]),
        confirmPassword: new FormControl(''),
        // recaptchaReactive: new FormControl(null, Validators.required),
      },
      {
        validator: ConfirmedValidator('password', 'confirmPassword')
      }
    );
    // this.registerForm.setValidators(this.checkUserExistByUserName())
  }

  checkUserExistByUserName(event) {
    const username = event.target.value;
    if (username.length > 0) {
      this.userService.checkUserExistByUsername(username).subscribe(res => {
        if (res) {
          this.controls.username.setErrors({usernameAlreadyExist: true});
        } else {
          delete this.controls.username.errors?.usernameAlreadyExist;
        }
      });
    }
  }

  noWhiteSpaceValidator(control: FormControl) {
    const isWhitespace = control.value.indexOf(' ') > 0;
    const isValid = !isWhitespace;
    return isValid ? null : {whitespace: true};
  }

  checkUserExistByEmail(event) {
    const email = event.target.value;
    if (email.length > 0) {
      this.userService.checkUserExistByEmail(email).subscribe(res => {
        if (res) {
          this.controls.email.setErrors({emailAlreadyExist: true});
        } else {
          delete this.controls.email.errors?.emailAlreadyExist;
        }
      });
    }
  }

  getUserFromForm() {
    return {
      username: this.controls.username.value,
      password: this.controls.password.value,
      email: this.controls.email.value
    };
  }

  register() {
    const obj = this.getUserFromForm();
    this.userService.register(obj).subscribe(
      res => {
        this.notificationService.showSuccess('Вы успешно зарегистрировались на Online-wallet.ru', 'Регистрация')
        this.authService.login(obj.username, obj.password).subscribe(result => {
          localStorage.setItem('username', result.username);
          localStorage.setItem('token', 'Bearer ' + result.token);
          this.authService.setCurrentUser(result);
        });
        this.dialogRef.close();
      },
      error => {
        this.notificationService.showError('Возникла ошибка при регистрации. Повторите попытку.', 'Регистрация')
        this.dialogRef.close();
      }
    );
  }

  public hasControlsErrors = (controlName: string, errorName: string) => {
    return this.controls[controlName].hasError(errorName);
  };

  close() {
    this.dialogRef.close();
  }
}
