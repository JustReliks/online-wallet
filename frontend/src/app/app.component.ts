import {Component, OnInit} from '@angular/core';
import {delay, filter} from 'rxjs/operators';
import {LoadingService} from "./service/loading.service";
import {NavigationEnd, NavigationStart, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'online-wallet';

  public loading = false;
  public isMainPage: boolean;

  constructor(
    private _loading: LoadingService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.listenToLoading();
  }

  listenToLoading(): void {
    this._loading.loadingSub
      .pipe(delay(0))
      .subscribe((loading) => {
        this.loading = loading;
      });
  }
}
