import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})

export class HistoryComponent implements OnInit {
// fields of html file
  filteredModel: string = "";
  data: string = "";
  data2: string = "";
  searchedText: string = "";
  models = []
  requests = [
    {userHash: '', model: '', request: '', response: '', createdAt: ''}
  ]
  url: string = "archive/filter"
  checker: boolean = false;

  p:number=1;
  count:number=10;

  constructor(private http: HttpClient) {
  }
// adds models and show all requests by default
  ngOnInit(): void {
    this.http.get(this.url)
    .subscribe({next: (data: any) => this.requests = data})
    this.http.get('bot/basic_models').subscribe({next: (model: any) => this.models = model})
  }
  getFilter(): void {
    // print filter parameters
    console.log("date from", this.data);
    console.log("date to", this.data2);
    console.log("model", this.filteredModel)
    console.log("text", this.searchedText)
    console.log("checker", this.checker);
    console.log("hash", localStorage.getItem('user-hash'))

// users requests
    if (this.checker) {
      this.http.get(this.url +
        "?userHash=" + localStorage.getItem('user-hash') +
        "&model=" + this.filteredModel +
        "&text=" + this.searchedText +
        "&dateFrom=" + this.data +
        "&dateTo=" + this.data2)
      .subscribe({next: (data: any) => this.requests = data})
// all requests
    } else
      this.http.get(this.url +
        "?userHash=" +
        "&model=" + this.filteredModel +
        "&text=" + this.searchedText +
        "&dateFrom=" + this.data +
        "&dateTo=" + this.data2)
      .subscribe({next: (data: any) => this.requests = data})
  }
}
