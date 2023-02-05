import { Component, OnInit } from '@angular/core';
import {DtoUser} from "../../../dto/dto.user";
import {UserService} from "../../services/user.services";

@Component({
  selector: 'app-auth',
  templateUrl: `./auth.component.html`,
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {
  name:any;
  login: any;
  password: any;

  constructor(private userService:UserService) { }

  ngOnInit(): void {  }

  getUser() {
    let user = new DtoUser(this.name,this.login,this.password);
    this.userService.createUsers(user).subscribe(dto => {
      user = dto;
    });
    console.log(user.toString())
  }
}
