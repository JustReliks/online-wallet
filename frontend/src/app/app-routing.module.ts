import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./components/pages/main/main.component";
import {FinanceComponent} from "./components/pages/finance/finance.component";
import {IncomeComponent} from "./components/pages/income/income.component";
import {SettingsComponent} from "./components/pages/settings/settings.component";
import {AuthGuard} from "./guard/auth.guard";

const routes: Routes = [
  {path: '', pathMatch: 'full', component: MainComponent},
  {path: 'finance', pathMatch: 'full', component: FinanceComponent},
  {path: 'income', pathMatch: 'full', component: IncomeComponent},
  {path: 'settings', pathMatch: 'full', component: SettingsComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: ''},]

@NgModule({
  imports: [RouterModule.forRoot(routes,)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
