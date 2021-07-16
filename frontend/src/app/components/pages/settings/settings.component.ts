import {Component, OnInit} from '@angular/core';
import {filter, take} from "rxjs/operators";
import {AuthService} from "../../../service/auth.service";
import {AuthUser} from "../../../entities/user";
import _ from "lodash";
import {LogoutEvent} from "../../../entities/auth.events";
import {ApplicationEventBroadcaster} from "../../../service/application.event.broadcaster";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  settingsState: SettingsState = SettingsState.START;
  private _originalUser: AuthUser;
  private _currentUser: AuthUser;

  constructor(private broadcaster: ApplicationEventBroadcaster,
              private authService: AuthService) {
    this.authService.getCurrentLoggedUser().pipe(filter(res => res != null), take(1)).subscribe(res => {
      this.user = res;
    });

    this.broadcaster.onType(LogoutEvent).subscribe(
      (e: LogoutEvent) => {
        this._originalUser = null;
      }
    );
  }

  set user(user: AuthUser) {
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  get user() {
    return this._currentUser;
  }

  ngOnInit(): void {
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }

  changeMenuState(state: string) {
    console.log(state, ' ', SettingsState[state]);
    this.settingsState = SettingsState[state];
  }

  onChangeState($event: SettingsState) {
    this.settingsState = $event;
  }
}

export enum SettingsState {
  START = "start", ACCOUNT = "account", SECURITY = "security"
}
