import { Component, OnInit } from '@angular/core';
import {DtoUser} from "../../dto/dto.user";
import {UserService} from "../services/user.services";

@Component({
  selector: 'app-auth',
  templateUrl: `./auth.component.html`,
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {


  constructor() { }

  ngOnInit(): void {  }


}
