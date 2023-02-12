import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {formatDate} from "@angular/common";
import {DtoFiltered} from "../../../dto/dto.filtered";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})

export class HistoryComponent implements OnInit {

  filteredModel: string = "BABBAGE";
  data:any;
  searchedText = '';
  models = []
  requests = [
    {userHash: '', model: '', request: '', response: '', createdAt: ''}
  ]
  filtered:any

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
   // this.http.get('archive/')
    this.http.get('archive/filter')
    .subscribe({next:(data:any) => this.requests=data});
    this.http.get('bot/basic_models').subscribe({next:(model:any) => this.models=model})
  }

  sendRequest(): void {
    this.http.get("archive/filter" + encodeURIComponent(JSON.stringify
    (new DtoFiltered(this.filteredModel,this.filteredModel,this.data,localStorage.getItem('user-hash')))))
      .subscribe({next:(data:any) => this.requests=data})
  }


  getFilter():void{
    console.log("Changed {}",this.data);
    console.log("Changed {}", this.filteredModel)
    console.log("Chosen {}",this.searchedText)
  }
}
