import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {DtoMess} from "../../dto/dto.mess";
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class MessService {

  userHash:any;


  private defaultApi: string = environment.appApi+"/bot"


  constructor(private http:HttpClient) { }

  getMessageResponse(mess:string):Observable<any>{
    let messBody = new DtoMess(mess,"ADA");
    this.userHash = localStorage.getItem('user-hash');
    const myHeader = new HttpHeaders().set('user-hash', this.userHash);
    console.log('Get from localStorage: \n' + this.userHash);
    return this.http.post<any>(this.defaultApi, messBody, {headers: myHeader, observe: 'response'
       });
  }


}
