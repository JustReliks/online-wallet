import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../service/auth.service";
import {filter, take} from "rxjs/operators";
import {AuthUser} from "../../../entities/user";
import _ from "lodash";

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.scss']
})
export class FinanceComponent implements OnInit {

  private _originalUser: AuthUser;
  private _currentUser: AuthUser;

  constructor(private _authService: AuthService) {
    this._authService.getCurrentLoggedUser().pipe(filter(res => res != null), take(1)).subscribe(res => {
      this.user = res;
    });
  }

  get user(): AuthUser {
    return this._currentUser;
  }

  set user(user: AuthUser) {
    this._originalUser = user;
    console.log(this._originalUser)
    this.cloneOriginalUser();
  }

  ngOnInit(): void {
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }
}
