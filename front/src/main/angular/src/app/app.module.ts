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
import {DeviceDetectorService} from "ngx-device-detector";

const appRoutes: Routes = [
  {path: 'chat', component: ChatComponent},
  {path: 'history', component: HistoryComponent}

]


@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    HistoryComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [MessService, UserService, DeviceDetectorService],
  bootstrap: [AppComponent]
})
export class AppModule { }
