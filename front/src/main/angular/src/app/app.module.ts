import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MessService} from "./services/mess.service";
import { ChatComponent } from './components/chat/chat.component';
import { HistoryComponent } from './components/history/history.component';
import {RouterModule, Routes} from "@angular/router";
import {UserService} from "./services/user.services";
import {AuthComponent} from "./auth.modul/auth.component";
import {AuthModule} from "./auth.modul/auth.module";

const appRoutes: Routes = [
  {path: 'chat', component: ChatComponent},
  {path: 'history', component: HistoryComponent},
  {path: 'auth', component: AuthComponent}
]


@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    HistoryComponent
  ],
  imports: [
    AuthModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [MessService, UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
