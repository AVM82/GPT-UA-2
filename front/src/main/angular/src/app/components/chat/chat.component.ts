import {
  Component, ComponentRef,
  OnInit,
  ViewChild,
  ViewContainerRef
} from '@angular/core';
import {MessService} from "../../services/mess.service";
import {RespComponent} from "../resp/resp.component";

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
  inMessUkr: string = '';
  responseUkr: string = 'Поле для відповіді!';
  hasUkrResp: boolean = false;
  tempResponse: string = "";
  moods: string[] = [];

  WAIT = 'Чекайте...';

  @ViewChild('resp', { read: ViewContainerRef }) respNew?: ViewContainerRef ;
  componentRef?: ComponentRef<RespComponent>;

  constructor(private messageServices: MessService) {
  }

  send() {
    if (this.inMess.trim().length!=0 ) {
      console.log(this.hasUkrResp)
      console.log(this.inMess);
      console.log("MODEL {}", this.modelSelect);
      this.response = this.WAIT
      this.addResp('Ви  : ' + this.inMess);
      this.addResp('֎   : ' + this.WAIT);
      this.messageServices.getMessageResponse(this.inMess, this.modelSelect).subscribe(
        resp => {
          this.response = resp.body.message;
          (<RespComponent>(this.componentRef?.instance)).respMess = '֎   : ' + this.response;
          this.tempResponse = resp.body.message;
          console.log(resp.headers.get('user-hash'));
          localStorage.setItem('user-hash', resp.headers.get('user-hash'));
          this.translateEnUkr();
          this.inMess = '';
        });
    }
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
    this.setMoods();
  }

  setMoods(): void {
    this.messageServices.getMoods().subscribe(
      data => {
        this.moods = data;
      })
  }

  translateUkrEn(): void {
    if (this.inMessUkr.trim().length!=0 ) {
      this.messageServices.translateUkrEn(this.inMessUkr).subscribe(response => {
        this.inMess = response.body.message.replaceAll('\n', '');
        this.inMessUkr = '';
        console.log('Get response!');
        localStorage.setItem('user-hash', response.headers.get('user-hash'));
      })
    }
  }

  translateEnUkr(): void {
    if (this.hasUkrResp && this.inMess !== ''
      && this.response !== this.WAIT && this.response !== this.DEFAULT) {
      this.addResp('֎ (Переклад): ' + this.WAIT);
      this.messageServices.translateEnUkr(this.response).subscribe(response => {
        this.responseUkr = response.body.message.replaceAll('\n', '');
        console.log('Отримав відповідь Українською!!');
        (<RespComponent>(this.componentRef?.instance)).respMess = '֎ (Переклад): ' + this.responseUkr;
      });
    }
  }

  addResp(message:string) {
    this.componentRef = this.respNew?.createComponent(RespComponent);
    (<RespComponent>(this.componentRef?.instance)).respMess = message;
  }


}
