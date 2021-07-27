import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./components/pages/main/main.component";
import {FinanceComponent} from "./components/pages/finance/finance.component";
import {SettingsComponent} from "./components/pages/settings/settings.component";
import {AuthGuard} from "./guard/auth.guard";
import {RestoreAccessComponent} from "./components/pages/restore-access/restore-access.component";
import {UserProfileModalComponent} from "./components/pages/user-profile-modal/user-profile-modal.component";

const routes: Routes = [
  {path: '', pathMatch: 'full', component: MainComponent},
  {path: 'finance', pathMatch: 'full', component: FinanceComponent, canActivate: [AuthGuard]},
  {path: 'settings', pathMatch: 'full', component: SettingsComponent, canActivate: [AuthGuard]},
  {path: 'restore', component: RestoreAccessComponent},
  {path: 'profile', component: UserProfileModalComponent},
  {path: '**', redirectTo: ''},]

@NgModule({
  imports: [RouterModule.forRoot(routes,)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
