import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthComponent} from "./auth.component";
import {BrowserModule} from "@angular/platform-browser";
import {AppRoutingModule} from "../app-routing.module";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule, Routes} from "@angular/router";
import {UserService} from "../services/user.services";
import { RegComponent } from './components/reg/reg.component';
import { LoginComponent } from './components/login/login.component';


const authRoutes:Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegComponent}
]

@NgModule({
  declarations: [AuthComponent, RegComponent, LoginComponent],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(authRoutes)
  ],
  providers: [UserService],
  bootstrap: [AuthComponent]
})
export class AuthModule { }
