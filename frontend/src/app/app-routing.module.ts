import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./components/pages/main/main.component";
import {FinanceComponent} from "./components/pages/fincance/finance.component";

const routes: Routes = [
  {path: '', pathMatch: 'full', component: MainComponent},
  {path: 'finance', pathMatch: 'full', component: FinanceComponent},
  {path: '**', redirectTo: ''},]

@NgModule({
  imports: [RouterModule.forRoot(routes,)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
