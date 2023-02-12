export class DtoFiltered{
  message:string;
  model:any;
  date:any;
  hashUser:any;


  constructor(message: string, model: any,date:any,hashUser:any) {
    this.message = message;
    this.model = model;
    this.date = date;
    this.hashUser = hashUser;
  }
}
