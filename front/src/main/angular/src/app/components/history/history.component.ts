import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})

export class HistoryComponent implements OnInit {

  requests = [
    {userHash: '', model: '', request: '', response: '', createdAt: ''}
  ]

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.http.get('archive/')
    .subscribe({next:(data:any) => this.requests=data});
  }
}
