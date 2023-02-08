import { Component } from '@angular/core';
import {DtoUser} from "../dto/dto.user";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  user: any;
    setUser(user:DtoUser):void {
    this.user = user;
  }
}
