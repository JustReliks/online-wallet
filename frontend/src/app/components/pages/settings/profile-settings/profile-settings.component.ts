import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthUser} from "../../../../entities/user";
import {UserService} from "../../../../service/user.service";
import {NotificationService} from "../../../../service/notification.service";
import _ from "lodash";
import {UserSettings} from "../../../../entities/user-settings";
import {filter, take} from "rxjs/operators";
import {SettingsState} from "../settings.component";
import {FileService} from "../../../../service/file.service";
import {DomSanitizer} from "@angular/platform-browser";
import {Base64img} from "../../../../util/base64img";
import {DictionaryService} from "../../../../service/dictionary.service";
import {Currency} from "../../../../entities/currency";

@Component({
  selector: 'app-profile-settings',
  templateUrl: './profile-settings.component.html',
  styleUrls: ['./profile-settings.component.scss']
})
export class ProfileSettingsComponent implements OnInit {

  @Output() changeState: EventEmitter<SettingsState> = new EventEmitter<SettingsState>();
  currency: string = 'RUB';
  profileImageSrc: any = ' ';
  profileFormGroup: FormGroup;
  private reader: FileReader;
  private _currentUser: AuthUser;
  private _userSettings: UserSettings;
  private _currencies: Array<Currency>;

  constructor(private _fb: FormBuilder,
              private _userService: UserService,
              private _notificationService: NotificationService,
              private _fileService: FileService,
              private _sanitizer: DomSanitizer,
              private _dictionaryService:DictionaryService
  ) {

    this._dictionaryService.getAllCurrencies().subscribe(res=>this._currencies= res);

    this.profileFormGroup = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      middleName: new FormControl('', [Validators.required]),
      about: new FormControl('', []),
      url: new FormControl('', []),
      email: new FormControl('', [Validators.email]),
      phone: new FormControl('', []),
      country: new FormControl('', []),
      language: new FormControl('', []),
      currency: new FormControl('', [Validators.required]),
      file: new FormControl(undefined, []),
    });
    this.reader = new FileReader();
    this._fileService.changeProfileImageSubjectObservable.subscribe(res => {
      if (res.state != 'new') {
        this.profileImageSrc = this.getProfileImg();
      }
    })
  }

  get currencies(): Array<Currency> {
    return this._currencies;
  }

  get user() {
    return this._currentUser;
  }

  get userSettings(): UserSettings {
    return this._userSettings;
  }

  set userSettings(value: UserSettings) {
    this._userSettings = value;
  }

  @Input('user') set user(user: AuthUser) {
    if (user) {
      this._currentUser = _.cloneDeep(user);
    }
  }

  get controls() {
    return this.profileFormGroup.controls;
  }

  ngOnInit(): void {
    this._userService.getUserSettings(this.user.id).pipe(filter(res => res != null), take(1)).subscribe(res => {
      this._userSettings = new UserSettings(res);
      this.initForm(this._userSettings);
    });
  }

  private initForm(userSettings: UserSettings) {
    this.controls.firstName.setValue(userSettings.firstName);
    this.controls.lastName.setValue(userSettings.lastName);
    this.controls.middleName.setValue(userSettings.middleName);
    this.controls.about.setValue(userSettings.about);
    this.controls.url.setValue(userSettings.url);
    this.controls.email.setValue(this.user.email);
    this.controls.phone.setValue(userSettings.phone);
    this.controls.country.setValue(userSettings.country);
    this.controls.language.setValue(userSettings.language);
    this.controls.currency.setValue(userSettings.currency);
    this.profileImageSrc = this.getProfileImg(userSettings);
    console.log(this.profileImageSrc)
    this.currency = userSettings.currency;
    this.reader.readAsDataURL(new Blob([userSettings.profileImage]));
  }

  hasControlsErrors(controlName: string, errorName: string) {
    return this.controls[controlName].hasError(errorName);
  }

  close() {
    this.changeState.emit(SettingsState.START);
  }

  save() {
    const currentFileUpload = this.getFile(this.profileFormGroup);
    this._userSettings.firstName = this.controls.firstName.value;
    this._userSettings.lastName = this.controls.lastName.value;
    this._userSettings.middleName = this.controls.middleName.value;
    this._userSettings.about = this.controls.about.value;
    this._userSettings.url = this.controls.url.value;
    this._userSettings.phone = this.controls.phone.value;
    this._userSettings.country = this.controls.country.value;
    this._userSettings.language = this.controls.language.value;
    this._userSettings.currency = this.currency;

    this._userService.updateUserProfile(this._userSettings).subscribe(res => {
      if (currentFileUpload) {
        this._fileService.saveProfileImage(this.user.id, currentFileUpload).subscribe(result => {
          this._fileService.changeProfileImage({
            source: this.getProfileImg(result.body),
            state: 'update'
          });
          this.initForm(result.body);
        })
      } else {
        this.initForm(res);
      }
      this._notificationService.showSuccess('Настройки успешно изменены.', 'Настройки аккаунта')
    }, error => {
      this._notificationService.showError('Возникла ошибка. Повторите попытку позже.', 'Настройки аккаунта')
    })
  }

  public getFile(fb: FormGroup): File {
    return fb.get('file').value?.files[0];
  }

  getProfileImg(profileImage?: any) {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${profileImage ? profileImage?.profileImage || Base64img.base64DefaultAvatar : this.userSettings?.profileImage || Base64img.base64DefaultAvatar}`);
  }
}
