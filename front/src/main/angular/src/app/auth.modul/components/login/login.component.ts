import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.services";
import {DtoUser} from "../../../../dto/dto.user";
import {AppComponent} from "../../../app.component";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  login: any;
  password: any;

  constructor(private userService:UserService, private appComp:AppComponent) {

  }

  ngOnInit(): void {

  }

  loginUser() {
    let user = new DtoUser("",this.login,this.password);
    this.userService.loginUser(user).subscribe(dto => {
      this.appComp.setUser(dto)
    });
  }
}
