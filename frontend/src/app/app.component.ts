import {Component, OnInit} from '@angular/core';
import {delay} from 'rxjs/operators';
import {LoadingService} from "./service/loading.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'online-wallet';

  public loading = false;

  constructor(
    private _loading: LoadingService
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
