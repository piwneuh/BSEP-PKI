import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdminHomePageComponent } from './admin-home-page/admin-home-page.component';
import { ClientHomePageComponent } from './client-home-page/client-home-page.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminHomePageComponent,
    ClientHomePageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
