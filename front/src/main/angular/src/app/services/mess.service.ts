import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {DtoMess} from "../../dto/dto.mess";
import {DtoMoodMess} from "../../dto/dtoMood.mess";

@Injectable({
  providedIn: 'root'
})
export class MessService {

  userHash: any;
  myHeader: any;

  private defaultApi: string = "bot"

  constructor(private http: HttpClient) {
  }

  getMessageResponse(mess: string, model: string): Observable<any> {
    let messBody = new DtoMess(mess, model);
    this.userHash = localStorage.getItem('user-hash') || '';
    this.myHeader = new HttpHeaders().set('user-hash', this.userHash);
    console.log('Get from localStorage: \n' + this.userHash);
    return this.http.post<any>(this.defaultApi, messBody, {
      headers: this.myHeader, observe: 'response'
    });
  }

  getModels(): Observable<any> {
    return this.http.get<any>(this.defaultApi + '/basic_models');
  }

  getMoods(): Observable<any> {
    return this.http.get<any>(this.defaultApi + '/moods');
  }

  translateUkrEn(mess: string): Observable<any> {
    let messBody = new DtoMess(mess, 'DAVINCI');
    return this.http.post<any>(this.defaultApi + '/translate/en', messBody, {
      headers: this.myHeader, observe: 'response'
    });
  }

  translateEnUkr(mess: string): Observable<any> {
    let messBody = new DtoMess(mess, 'DAVINCI');
    return this.http.post<any>(this.defaultApi + '/translate/uk', messBody, {
      headers: this.myHeader, observe: 'response'
    });
  }

  getMoodMessageResponse(mess: string, model: string, mood: string): Observable<any> {
    let messBody = new DtoMoodMess(mess, model, mood);
    console.log('GET MESSAGE : {}',messBody.message);
    this.userHash = localStorage.getItem('user-hash') || '';
    this.myHeader = new HttpHeaders().set('user-hash', this.userHash);
    console.log('Get MOOD from localStorage: \n' + this.userHash);
    return this.http.post<any>(this.defaultApi + '/completions/mood', messBody, {
      headers: this.myHeader, observe: 'response'
    });
  }
}
