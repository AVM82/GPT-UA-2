import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.services";
import {DtoUser} from "../../../../dto/dto.user";
import {AppComponent} from "../../../app.component";

@Component({
  selector: 'app-reg',
  templateUrl: './reg.component.html',
  styleUrls: ['./reg.component.scss']
})
export class RegComponent implements OnInit {

  name:any;
  login: any;
  password: any;

  constructor(private userService:UserService, private appComp:AppComponent) { }

  ngOnInit(): void {  }

  getUser() {
    let user = new DtoUser(this.name,this.login,this.password);
    this.userService.createUsers(user).subscribe(dto => {
      user = dto;
    });
    console.log(user.toString());
    this.appComp.setUser(user);
  }

}
