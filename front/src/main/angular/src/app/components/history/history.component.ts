import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})

export class HistoryComponent implements OnInit {
  filteredModel: string = "BABBAGE";
  data: any;
  searchedText = '';
  models = []
  requests = [
    {userHash: '', model: '', request: '', response: '', createdAt: ''}
  ]
  url: string = "archive/filter";

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.http.get('bot/basic_models').subscribe({next: (model: any) => this.models = model})
  }

  getFilter(): void {
    console.log("Changed {}", this.data);
    console.log("Changed {}", this.filteredModel)
    console.log("Chosen {}", this.searchedText)
    console.log("Chosen {}", localStorage.getItem('user-hash'))
    this.http.get(this.url +
      "?userHash=" +
      "&model=" + this.filteredModel +
      "&text=" + this.searchedText + "&date=" + this.data)
    .subscribe({next: (data: any) => this.requests = data})
  }
}
