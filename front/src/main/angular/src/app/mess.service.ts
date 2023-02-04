import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {DtoMess} from "../dto/dto.mess";


@Injectable({
  providedIn: 'root'
})
export class MessService {

  private defaultApi: string = `http://gptua-env.eba-kmwv8wpt.eu-central-1.elasticbeanstalk.com/bot?mess=`;

  constructor(private http:HttpClient) { }

  getMessageResponse(mess:string):Observable<DtoMess>{
    return this.http.get<DtoMess>(this.defaultApi + mess);
  }


}
