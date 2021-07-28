import {Component, ElementRef, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef} from "@angular/material/dialog";
import {AuthUser} from "../../../entities/user";
import {UserService} from "../../../service/user.service";
import {Base64img} from "../../../util/base64img";
import {DomSanitizer} from "@angular/platform-browser";
import {UserSettings} from "../../../entities/user-settings";
import {AccountService} from "../../../service/account.service";
import {ConvertedBalance} from "../../../entities/converted-balance";
import {AuthService} from "../../../service/auth.service";

@Component({
  selector: 'app-user-profile-modal',
  templateUrl: './user-profile-modal.component.html',
  styleUrls: ['./user-profile-modal.component.scss']
})
export class UserProfileModalComponent implements OnInit {
   positionRelativeToElement: ElementRef;
  user: AuthUser;
  userSettings: UserSettings;
  convertedBalance: ConvertedBalance;
  accountsCount: number = 0;

  constructor(public dialogRef: MatDialogRef<UserProfileModalComponent>,
              @Inject(MAT_DIALOG_DATA) public options: { positionRelativeToElement: ElementRef, user: AuthUser },
              private userService: UserService,
              private accountService: AccountService,
              private _sanitizer: DomSanitizer,
              private authService: AuthService) {

    this.positionRelativeToElement = options.positionRelativeToElement
    this.user = options.user;
    if (this.user == null) {
      this.authService.getCurrentLoggedUser().subscribe(res => this.user = res);
    }
    if (this.user != null) {
      this.userService.getUserSettings(this.user.id).subscribe(res => this.userSettings = res);
      this.accountService.getBalance(this.user.id, null).subscribe(res => this.convertedBalance = res);
      this.accountService.getAccountsCount(this.user.id).subscribe(res => this.accountsCount = res);
    }
  }

  ngOnInit() {
    const matDialogConfig = new MatDialogConfig()
    const rect: DOMRect = this.positionRelativeToElement.nativeElement.getBoundingClientRect()

    matDialogConfig.position = {right: `${rect.right - 1130}px`, top: `${rect.bottom + 15}px`}
    this.dialogRef.updatePosition(matDialogConfig.position)
  }

  getImgSrc() {

  }

  getProfileImage() {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${this.userSettings?.profileImage || Base64img.base64DefaultAvatar}`);
  }
}
