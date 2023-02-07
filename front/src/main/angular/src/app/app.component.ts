import { Component } from '@angular/core';
import {DtoUser} from "../dto/dto.user";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  user: string = '';


  chat() {

  }

  history() {

  }

  auth() {

  }


    setUser(user:DtoUser):void {
    this.user = 'Login as '+user.hash;
  }
}
