export class DtoMoodMess{
  message:string;
  model:any;
  mood:any;

  constructor(message: string, model: any, mood: any) {
    this.message = message;
    this.model = model;
    this.mood = mood;
  }
}
