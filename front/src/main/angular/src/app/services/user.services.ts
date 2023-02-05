import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {DtoMess} from "../../dto/dto.mess";
import {DtoUser} from "../../dto/dto.user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private defaultApi: string = `http://gptua-env.eba-kmwv8wpt.eu-central-1.elasticbeanstalk.com/users`;

  constructor(private http:HttpClient) { }

  createUsers(dtoUser:DtoUser):Observable<DtoUser>{
    return this.http.post<DtoUser>(this.defaultApi,dtoUser);
  }

  loginUser(dtoUser:DtoUser):Observable<DtoUser>{
    return this.http.post<DtoUser>(this.defaultApi+'/login',dtoUser);
  }
}
