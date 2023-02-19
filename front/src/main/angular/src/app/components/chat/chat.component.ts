import {Component, OnInit} from '@angular/core';
import {MessService} from "../../services/mess.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  DEFAULT = "Field for response";
  inMess: string = '';
  response: string = this.DEFAULT;
  models: string[] = [];
  modelSelect: string = "DAVINCI";
  inMessUkr: string = 'Що таке ';
  responseUkr: string = 'Поле для відповіді!';
  hasUkrResp: boolean = false;
  tempResponse: string = "";

  WAIT = 'Wait...';

  constructor(private messageServices: MessService) {
  }

  send() {
    console.log(this.hasUkrResp)
    console.log(this.inMess);
    console.log("MODEL {}", this.modelSelect);
    this.response = this.WAIT

    this.messageServices.getMessageResponse(this.inMess, this.modelSelect).subscribe(
      resp => {
        this.response = resp.body.message;
        this.tempResponse=resp.body.message;
        console.log(resp.headers.get('user-hash'));
        localStorage.setItem('user-hash', resp.headers.get('user-hash'));
        this.translateEnUkr();
      });
  }

  setMood(mood: string) {
    console.log('MOOD = {}', mood);
    console.log('TEMP MESSAGE = {}', this.tempResponse);
    this.messageServices.getMoodMessageResponse(this.tempResponse, this.modelSelect, mood).subscribe(
      resp => {
        this.response = resp.body.message;
        localStorage.setItem('user-hash', resp.headers.get('user-hash'));
      });
  }

  ngOnInit(): void {
    this.messageServices.getModels().subscribe(response => {
      this.models = response;
      console.log('get response {}', this.models);
    })
  }

  translateUkrEn(): void {
    this.messageServices.translateUkrEn(this.inMessUkr).subscribe(response => {
      this.inMess = response.body.message.replaceAll('\n', '');
      console.log('Get response!');
      localStorage.setItem('user-hash', response.headers.get('user-hash'));
    })
  }

  translateEnUkr(): void {
    if (this.hasUkrResp && this.inMess !== ''
      && this.response !== this.WAIT && this.response !== this.DEFAULT) {
      this.messageServices.translateEnUkr(this.response).subscribe(response => {
        this.responseUkr = response.body.message.replaceAll('\n', '');
        console.log('Отримав відповідь Українською!!');
      });
    }
  }
}
