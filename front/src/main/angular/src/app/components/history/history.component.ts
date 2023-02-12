import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})

export class HistoryComponent implements OnInit {

  data:any;
  requests = [
    {userHash: '', model: '', request: '', response: '', createdAt: ''}
  ]

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
   // this.http.get('archive/')
    this.http.get('archive/filter')
    .subscribe({next:(data:any) => this.requests=data});
  }


  getFilter():void{
    console.log("Changed {}",this.data);
  }
}
