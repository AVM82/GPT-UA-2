import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {DtoMess} from "../../dto/dto.mess";
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class MessService {

  private defaultApi: string = environment.appApi+"/bot?mess="

  constructor(private http:HttpClient) { }

  getMessageResponse(mess:string):Observable<DtoMess>{
    return this.http.get<DtoMess>(this.defaultApi + mess);
  }


}
