import { Component, OnInit } from '@angular/core';
import {MessService} from "../../services/mess.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  inMess = 'I\'d ....' ;
  response:string = "Field for response";
  constructor(private messageServices:MessService) {
  }

  send() {
    console.log(this.inMess)
    this.response = 'Wait...'
    this.messageServices.getMessageResponse(this.inMess).subscribe(
      resp => {
        this.response = resp.body.message;
          console.log(resp.headers.get('user-hash'))
          localStorage.setItem('user-hash', resp.headers.get('user-hash'));
      });

  }

  ngOnInit(): void {
  }

}
