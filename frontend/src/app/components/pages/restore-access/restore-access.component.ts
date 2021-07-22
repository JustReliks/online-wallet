import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MatDialogRef} from "@angular/material/dialog";
import {AuthService} from "../../../service/auth.service";
import {NotificationService} from "../../../service/notification.service";
import {RestoreService} from "../../../service/restore.service";

@Component({
  selector: 'app-restore-access',
  templateUrl: './restore-access.component.html',
  styleUrls: ['./restore-access.component.scss']
})
export class RestoreAccessComponent implements OnInit {
  get usernameEmail(): string {
    return this._usernameEmail;
  }

  set usernameEmail(value: string) {
    this._usernameEmail = value;
  }

  restoreAccessForm: FormGroup;
  private _usernameEmail: string;

  constructor(private authService: AuthService,
              private router: Router,
              private _fb: FormBuilder,
              private route: ActivatedRoute,
              private restoreService: RestoreService,
              private notificationService: NotificationService,
              public dialogRef: MatDialogRef<RestoreAccessComponent>,) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params.action === 'reset-password' || params.action === 'reset-two-factor') {
        this.restoreService.restoreAccessRequestGet(params.userId, params.action, params.token).subscribe(res => {
          this.notificationService.showSuccess(res.message, 'Восстановление доступа')
        }, error => {
          this.notificationService.showError(error.error.message, 'Восстановление доступа')
        })
      }
      this.router.navigate(['/']);
    });
    if (this.authService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    } else {
      this.restoreAccessForm = this._fb.group({
        username: ['', [Validators.required]],
        recaptchaReactive: new FormControl(null, Validators.required),
      });
    }
  }

  usernameKeyUp($event: any) {
    this.usernameEmail = $event.target.value;
  }

  restore() {
    this.restoreService.restoreAccessRequest(this.usernameEmail).subscribe(res => {
      this.notificationService.showSuccess(res.message, 'Восстановление доступа')
      this.dialogRef.close();
    }, error => {
      this.notificationService.showError(error.error.message, 'Восстановление доступа')
    })
  }
}
