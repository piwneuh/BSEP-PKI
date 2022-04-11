import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminHomePageComponent } from './admin-home-page/admin-home-page.component';
import { CertificateDetailsComponent } from './certificate-details/certificate-details.component';
import { ClientHomePageComponent } from './client-home-page/client-home-page.component';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  {path:'', component: LoginComponent},
  {path:'admin', component: AdminHomePageComponent},
  {path:'client', component: ClientHomePageComponent },
  {path:'admin/create', component: CreateCertificateComponent},
  {path:'admin/details', component: CertificateDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
