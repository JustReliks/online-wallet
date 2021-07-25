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
import {ProfileSettingsComponent} from "./components/pages/settings/profile-settings/profile-settings.component";
import {SecuritySettingsComponent} from "./components/pages/settings/security-settings/security-settings.component";
import {AccountsComponent} from "./components/pages/accounts/accounts.component";
import {CreateAccountComponent} from "./components/pages/accounts/create-account/create-account.component";
import {MaterialFileInputModule} from 'ngx-material-file-input';
import {AngularGradientProgressbarModule} from "angular-gradient-progressbar";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {AddTransactionModalComponent} from "./components/pages/finance/add-transaction-modal/add-transaction-modal.component";
import {TransactionHistoryComponent} from "./components/pages/finance/transaction-history/transaction-history.component";
import {HotTableModule} from "@handsontable/angular";
import {RECAPTCHA_SETTINGS, RecaptchaFormsModule, RecaptchaModule, RecaptchaSettings} from 'ng-recaptcha';
import 'handsontable/languages/ru-RU';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {AccountSettingsComponent} from "./components/pages/accounts/account-settings/account-settings.component";
import {AccountsStatisticComponent} from "./components/pages/finance/accounts-statistic/accounts-statistic.component";
import {MatExpansionModule} from "@angular/material/expansion";
import {ChartModule} from "angular-highcharts";
import {RestoreAccessComponent} from "./components/pages/restore-access/restore-access.component";
import {MatStepperModule} from "@angular/material/stepper";
import {TypeInfoComponent} from "./components/pages/accounts/create-account/type-info/type-info.component";

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
    SelectAccountComponent,
    SettingsComponent,
    ProfileSettingsComponent,
    SecuritySettingsComponent,
    AccountsComponent,
    CreateAccountComponent,
    AddTransactionModalComponent,
    TransactionHistoryComponent,
    AccountSettingsComponent,
    AccountsStatisticComponent,
    RestoreAccessComponent,
    TypeInfoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatNativeDateModule,
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
    MaterialFileInputModule,
    AngularGradientProgressbarModule,
    MatDatepickerModule,
    HotTableModule,
    MatProgressBarModule,
    MatExpansionModule,
    ChartModule,
    RecaptchaFormsModule,
    RecaptchaModule,
    MatStepperModule

  ],
  providers: [
    AuthService,
    ApplicationEventBroadcaster,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: MAT_DIALOG_DATA, useValue: {}},
    {provide: MatDialogRef, useValue: {}},
    {provide: LOCALE_ID, useValue: 'ru'},
    {provide: APP_BASE_HREF, useValue: '/'},
    {
      provide: RECAPTCHA_SETTINGS,
      useValue: {siteKey: '6Lej1r0bAAAAAGTnG7iokT6NaVgMS3Nj673xdcgt'} as RecaptchaSettings,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
