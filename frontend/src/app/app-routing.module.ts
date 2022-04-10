import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminHomePageComponent } from './admin-home-page/admin-home-page.component';
import { ClientHomePageComponent } from './client-home-page/client-home-page.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  {path:'login', component: LoginComponent},
  {path:'admin', component: AdminHomePageComponent},
  {path:'client', component: ClientHomePageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
