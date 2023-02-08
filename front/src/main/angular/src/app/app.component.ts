import {Component, OnInit} from '@angular/core';
import {DeviceDetectorService} from "ngx-device-detector";



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{


  constructor(private deviceDetectorService:DeviceDetectorService) {
  }

  ngOnInit(): void {
    let devInfo = this.deviceDetectorService.getDeviceInfo()
    console.log(devInfo.userAgent)
    console.log(devInfo.device)
    console.log(devInfo.orientation)
    console.log(devInfo.os_version)
    console.log(devInfo.browser_version)

  }
}
