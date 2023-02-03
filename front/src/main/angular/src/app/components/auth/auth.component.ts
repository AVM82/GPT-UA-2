import { Component, OnInit } from '@angular/core';
import {DtoUser} from "../../../dto/dto.user";

@Component({
  selector: 'app-auth',
  templateUrl: `./auth.component.html`,
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {
  name:any;
  login: any;
  password: any;

  constructor() { }

  ngOnInit(): void {
  }

  getUser() {
    let user = new DtoUser(this.name,this.login,this.password);
    console.log(user.toString())
  }
}
