import { Component } from '@angular/core';
import {MessService} from "./mess.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'I\'d ....' ;
  response:string = "Field for response";


  constructor(private messageServices:MessService) {
  }

  send() {
    console.log(this.title)
    this.response='Wait...'
    this.messageServices.getMessageResponse(this.title).subscribe(
      resp => {
        this.response = resp.message;
      });



  }
}
