export class DtoUser{
  hash:string;

  constructor(hash: string) {
    this.hash = hash;

  }
  toString():string{
    return 'user{hash:'+this.hash+'}'
  }
}
