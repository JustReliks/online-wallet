import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HeaderComponent} from './components/common/header/header.component';
import {FooterComponent} from './components/common/footer/footer.component';
import {MiddleComponent} from './components/common/middle/middle.component';
import {RouterModule} from "@angular/router";
import {MainComponent} from './components/pages/main/main.component';
import {AuthService} from "./service/auth.service";
import {ApplicationEventBroadcaster} from "./service/application.event.broadcaster";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptor} from "./interceptors/auth.interceptor";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {APP_BASE_HREF, registerLocaleData} from "@angular/common";
import localeRu from '@angular/common/locales/ru';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MaterialModule} from "./material.module";
import {AppRoutingModule} from "./app-routing.module";
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {TwoFactorModalComponent} from "./components/common/login/two-factor-modal/two-factor-modal.component";
import {RegistrationComponent} from "./components/common/registration/registration.component";
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {LoginComponent} from "./components/common/login/login.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {ToastrModule} from "ngx-toastr";
import {FinanceComponent} from "./components/pages/finance/finance.component";
import {MatDividerModule} from "@angular/material/divider";
import {IncomeComponent} from "./components/pages/income/income.component";
import {MenuComponent} from "./components/pages/finance/menu/menu.component";
import {CardComponent} from "./components/pages/finance/card/card.component";
import {SelectAccountComponent} from "./components/pages/finance/select-account/select-account.component";
import {MatSelectModule} from "@angular/material/select";
import {SettingsComponent} from "./components/pages/settings/settings.component";
import {AccountSettingsComponent} from "./components/pages/settings/account-settings/account-settings.component";
import {SecuritySettingsComponent} from "./components/pages/settings/security-settings/security-settings.component";
import {AccountsComponent} from "./components/pages/accounts/accounts.component";
import {CreateAccountComponent} from "./components/pages/accounts/createaccount/createaccount.component";

registerLocaleData(localeRu, 'ru');

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    MiddleComponent,
    MainComponent,
    TwoFactorModalComponent,
    RegistrationComponent,
    LoginComponent,
    FinanceComponent,
    IncomeComponent,
    MenuComponent,
    CardComponent,
    SettingsComponent,
    AccountSettingsComponent,
    SecuritySettingsComponent,
    SelectAccountComponent,
    AccountsComponent,
    CreateAccountComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    // ServiceWorkerModule.register('ngsw-worker.js', {
    //   enabled: environment.production,
    //   // Register the ServiceWorker as soon as the app is stable
    //   // or after 30 seconds (whichever comes first).
    //   registrationStrategy: 'registerWhenStable:30000'
    // }),
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right',
    }),
    RouterModule,
    BrowserAnimationsModule,
    NgbModule,
    MaterialModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatFormFieldModule,
    MatTooltipModule,
    MatDividerModule,
    MatSelectModule,
  ],
  providers: [
    AuthService,
    ApplicationEventBroadcaster,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: MAT_DIALOG_DATA, useValue: {}},
    {provide: MatDialogRef, useValue: {}},
    {provide: LOCALE_ID, useValue: 'ru'},
    {provide: APP_BASE_HREF, useValue: '/'},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
