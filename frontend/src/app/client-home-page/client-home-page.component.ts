import { Component, OnInit } from '@angular/core';
import { CertificateService } from '../certificate.service';

@Component({
  selector: 'app-client-home-page',
  templateUrl: './client-home-page.component.html',
  styleUrls: ['./client-home-page.component.css']
})
export class ClientHomePageComponent implements OnInit {
  certificates: any

  constructor(private _service: CertificateService) { }

  ngOnInit(): void {
    this.getCertificatesByUsername()
  }

  getCertificatesByUsername(){
    this._service.getCertificatesByUsername().subscribe(f => {this.certificates = f})
  }

}
