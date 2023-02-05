import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.services";
import {DtoUser} from "../../../../dto/dto.user";
import {AppComponent} from "../../../app.component";
import {Routes} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  login: any;
  password: any;


  constructor(private userService:UserService, private appComp:AppComponent) { }

  ngOnInit(): void {
  }

  loginUser() {
    let user = new DtoUser("123",this.login,this.password);
    this.userService.loginUser(user).subscribe(dto => {
      user = dto;
    });
    this.appComp.setUser(user)
    console.log(user.toString())

  }
}
