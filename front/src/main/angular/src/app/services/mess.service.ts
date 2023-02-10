import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {DtoMess} from "../../dto/dto.mess";
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class MessService {

  private defaultApi: string = environment.appApi+"/bot?mess="

  constructor(private http:HttpClient) { }

  getMessageResponse(mess:string):Observable<any>{
    let messBody = new DtoMess(mess,"");
    let userHash = localStorage.getItem('user-hash') || 'first';
    const myHeader = new HttpHeaders().set('user-hash', userHash);
    console.log('Get from localStorage: \n' + userHash);
    return this.http.post<any>(this.defaultApi, messBody, {headers: myHeader, observe: 'response'
       });
  }


}
