import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./components/common/pages/main/main.component";

const routes: Routes = [{
  path: '', pathMatch: 'full', component: MainComponent
},
  {path: '**', redirectTo: ''},]

@NgModule({
  imports: [RouterModule.forRoot(routes,)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
