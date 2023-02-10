export class DtoMess{
  message:string;
  model:any;


  constructor(message: string, model: any) {
    this.message = message;
    this.model = model;
  }
}
