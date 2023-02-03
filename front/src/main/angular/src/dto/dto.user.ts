export class DtoUser{
  name:string;
  login:string;
  password:string;


  constructor(name: string, login: string, password: string) {
    this.name = name;
    this.login = login;
    this.password = password;
  }
  toString():string{
    return 'user{name:'+this.name+', login:'+this.login+'}'
  }
}
