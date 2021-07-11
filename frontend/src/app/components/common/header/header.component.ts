import {Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {NavigationEnd, NavigationStart, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {filter} from 'rxjs/operators';
import {environment} from '../../../../environments/environment';
import {AuthService} from "../../../service/auth.service";
import {ApplicationEventBroadcaster} from "../../../service/application.event.broadcaster";
import {AuthSuccessEvent, LogoutEvent} from "../../../entities/auth.events";
import {AuthUser, UserRoleEnum} from "../../../entities/user";
import {LoginComponent} from "../login/login.component";
import {RegistrationComponent} from "../registration/registration.component";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isAuthenticated = false;
  userName = '';
  @ViewChild('vkGroupsElem') vkGroupsElem: ElementRef;
  private siteDomain = environment.siteDomain;
  private linkPicture;
  private timeStamp: any;

  constructor(private router: Router,
              private authService: AuthService,
              private broadcaster: ApplicationEventBroadcaster,
              private dialog: MatDialog,
              private renderer2: Renderer2) {
    this.broadcaster.onType(AuthSuccessEvent).pipe(filter(user => user != null)).subscribe(
      (e: AuthSuccessEvent) => {
        this.user = e.user;
        this.updateHeadTimeStamp();
        this.userName = e.user.username;
        this.isAuthenticated = true;

      }
    );
    this.broadcaster.onType(LogoutEvent).subscribe(
      (e: LogoutEvent) => {
        this._user = null;
        this.userName = '';
        this.isAuthenticated = false;
      }
    );

    // this.skinService.changeSkinSubjectObservable.subscribe(res => {
    //   this.updateHeadTimeStamp();
    // })
    if (!this.user) {
      this.user = this.authService.getCurrentUser();
    }

    router.events.pipe<NavigationStart>(filter<NavigationStart>(event => event instanceof NavigationEnd
    )).subscribe(res => {
      if (res.url === '/') {
        setTimeout(() => {
          if (this.vkGroupsElem?.nativeElement) {
            this.initVkWidget();
          }
        }, 100);
      }
    });
  }

  private _user: AuthUser;

  get user(): AuthUser {
    return this._user;
  }

  set user(value: AuthUser) {
    this._user = value;
  }

  public logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnInit() {
    if (this.vkGroupsElem?.nativeElement) {
      // console.log("FQWFQWFWQ")
      this.initVkWidget()
    }
  }

  hasRoute(route: string) {
    return this.router.url === route;
  }

  login() {
    const dialogRef = this.dialog.open(LoginComponent, {
      width: '340px',
      data: {}
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }

  register() {
    const dialogRef = this.dialog.open(RegistrationComponent, {
      width: '450px',
      data: {}
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }

  getUserHead() {
    if (this.timeStamp) {
      return this.linkPicture + '?' + this.timeStamp;
    }
    return this.linkPicture;
  }

  public updateHeadTimeStamp() {
    this.linkPicture = `${this.siteDomain}/upload/minecraft/heads/${this._user?.username}.png`
    this.timeStamp = (new Date()).getTime();
  }

  isAdmin(){
    return this.user.getRole(UserRoleEnum.ADMIN);
  }

  private initVkWidget() {
    const srcScript = this.renderer2.createElement('script');
    srcScript.type = 'text/javascript';
    srcScript.text = `
           VK.Widgets.Group("vk_groups", {mode: 4, no_cover: 1, width: "300", height: "400", color3: '2C2D2E'}, 33986902);
    `;
    this.renderer2.appendChild(this.vkGroupsElem.nativeElement, srcScript);
  }
}
