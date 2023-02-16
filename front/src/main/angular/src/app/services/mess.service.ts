import {Injectable, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {DtoMess} from "../../dto/dto.mess";

@Injectable({
  providedIn: 'root'
})
export class MessService {

  userHash:any;
  myHeader:any;

  private defaultApi: string = "bot"

  constructor(private http:HttpClient) { }

  getMessageResponse(mess:string,model:string):Observable<any>{
    let messBody = new DtoMess(mess,model);
    this.userHash = localStorage.getItem('user-hash') || '';
    this.myHeader = new HttpHeaders().set('user-hash', this.userHash);
    console.log('Get from localStorage: \n' + this.userHash);
    return this.http.post<any>(this.defaultApi, messBody, {headers: this.myHeader, observe: 'response'
       });
  }

  getModels():Observable<any>{
    return this.http.get<any>(this.defaultApi+'/basic_models');
  }

  translateUkrEn(mess:string):Observable<any> {
    let messBody = new DtoMess(mess,'CURIE');
    return this.http.post<any>(this.defaultApi+'/translate/en', messBody, {
      headers: this.myHeader, observe: 'response'
    });
  }

  translateEnUkr(mess:string):Observable<any> {
    let messBody = new DtoMess(mess,'CURIE');
    return this.http.post<any>(this.defaultApi+'/translate/uk', messBody, {
      headers: this.myHeader, observe: 'response'
    });
  }
}
