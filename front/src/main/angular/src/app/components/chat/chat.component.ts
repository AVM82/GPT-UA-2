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
  userHash:string = "";
  constructor(private messageServices:MessService) {
  }

  send() {
    console.log(this.inMess)
    this.response = 'Wait...'
    this.messageServices.getMessageResponse(this.inMess).subscribe(
      resp => {
        this.response = resp.message;
        this.userHash = resp.headers.get('user-hash').toString();
      });
    console.log(this.userHash)
  }

  ngOnInit(): void {
  }

}
