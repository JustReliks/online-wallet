import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {AuthUser} from "../entities/user";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import 'rxjs/add/operator/filter';
import {AuthSuccessEvent, LogoutEvent} from "../entities/auth.events";
import {ApplicationEventBroadcaster} from "./application.event.broadcaster";
import {HttpClient} from "@angular/common/http";
import {filter} from "rxjs/operators";
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = environment.authUrl;

  private _user: AuthUser;
  private authenticationState = new Subject<any>();
  private currentUserSubject = new BehaviorSubject<AuthUser>(null);

  constructor(
    private http: HttpClient,
    private broadcaster: ApplicationEventBroadcaster,
  ) {
    this.broadcaster
      .onType(AuthSuccessEvent)
      .subscribe((e: AuthSuccessEvent) => {
        this._user = e.user;
      });
    this.broadcaster.onType(LogoutEvent).subscribe((e: LogoutEvent) => {
      // console.log('Logout event')
      this._user = null;
    });
  }

  public login(username, password): Observable<AuthUser> {
    // console.log(
    //    'Auth service: Login, username ',
    //    username,
    //    ' password',
    //    password
    //  );
    return this.http
      .post<AuthUser>(this.authUrl, {
        username,
        password: btoa(password)
      }).pipe(filter(res => res != null));
  }

  public loginTwoFactor(username: string, password: string, key: number): Observable<AuthUser> {
    return this.http
      .post<AuthUser>(this.authUrl + '/two-factor', {
        username,
        password: btoa(password),
        twoFactorKey: key
      }).pipe(filter(res => res != null));
  }

  public logout(): void {
    this._user = null;
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    const logoutEvent = new LogoutEvent();
    this.broadcaster.next(logoutEvent);
    this.authenticationState.next(logoutEvent);
  }

  public getCurrentUser(): AuthUser {
    return this.isUserLoggedIn() ? this._user : null;
  }

  public getCurrentLoggedUser(): Observable<AuthUser> {
    return this.currentUserSubject.asObservable();
  }

  public updateCurrentUser(): Observable<AuthUser> {
    return this.getCurrentAuthUser();
  }

  isUserLoggedIn(): boolean {
    // console.trace('state')
    const user = localStorage.getItem('username');
    const token = localStorage.getItem('token');

    if (!token) {
      this.broadcaster.next(new LogoutEvent());
    }
    this.getCurrentAuthUser().subscribe(
      res => {
        this.setCurrentUser(res);
      },
      error => {
        localStorage.removeItem('username');
        localStorage.removeItem('token');
        this.broadcaster.next(new LogoutEvent());
      }
    );
    return user !== null && token !== null && this._user !== null;
  }

  public setCurrentUser(authUser: AuthUser): void {
    authUser = new AuthUser(authUser);
    const loginEvent = new AuthSuccessEvent(authUser);
    this._user = authUser;

    // console.log('Set current user method called')
    // Events
    this.broadcaster.next(loginEvent);
    if (this._user) {
      //  console.log(this._user)
      this.currentUserSubject.next(this._user);
    }
    this.authenticationState.next(this._user);
  }

  private getCurrentAuthUser(): Observable<AuthUser> {
    return this.http.get<AuthUser>(this.authUrl + '/current');
  }
}
