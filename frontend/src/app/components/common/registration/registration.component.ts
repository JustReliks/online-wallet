import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {LoginComponent} from '../login/login.component';
import {AuthService} from "../../../service/auth.service";
import {UserService} from "../../../service/user.service";
import {NotificationService} from "../../../service/notification.service";
import {DictionaryService} from "../../../service/dictionary.service";
import {Currency} from "../../../entities/currency";

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
  personalInfo: FormGroup;
  registerForm: FormGroup;
  private _currencies: Array<Currency>;
  isGenerateDemoAccounts: boolean = false;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<LoginComponent>,
    private authService: AuthService,
    private notificationService: NotificationService,
    private dictionaryService: DictionaryService
  ) {
    this.dictionaryService.getAllCurrencies().subscribe(res => this._currencies = res);
  }

  get controls() {
    return this.registerForm.controls;
  }

  get form() {
    return this.registerForm;
  }

  get currencies(): Array<Currency> {
    return this._currencies;
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
      },
      {
        validator: ConfirmedValidator('password', 'confirmPassword')
      }
    );
    this.personalInfo = this.formBuilder.group(
      {
        firstName: new FormControl('', [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(17)
        ]),
        middleName: new FormControl('', [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(17)
        ]),
        lastName: new FormControl('', [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(17)
        ]),
        mainCurrency: new FormControl('', [Validators.required]),
        recaptchaReactive: new FormControl(null, [Validators.required]),
      },
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
      email: this.controls.email.value,
      firstName: this.personalInfo.controls.firstName.value,
      lastName: this.personalInfo.controls.lastName.value,
      middleName: this.personalInfo.controls.middleName.value,
      currency: this.personalInfo.controls.mainCurrency.value,
    };
  }

  register() {
    const obj = this.getUserFromForm();
    this.userService.register(obj,this.isGenerateDemoAccounts).subscribe(
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

  public hasControlsErrors = (fg: FormGroup, controlName: string, errorName: string) => {
    return fg.controls[controlName].hasError(errorName);
  };

  close() {
    this.dialogRef.close();
  }

  changeToggle() {
    this.isGenerateDemoAccounts = !this.isGenerateDemoAccounts;
  }
}
