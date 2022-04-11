import { Component, OnInit } from '@angular/core';
import { Certificate } from '../certificate';
import { CertificateService } from '../certificate.service';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.css']
})
export class CreateCertificateComponent implements OnInit {

  newCertificate: Certificate = new Certificate()


  constructor(private _service: CertificateService) { }

  ngOnInit(): void {
  }

  createSertificate(){
    this._service.createCertificate(this.newCertificate).subscribe()
  }  

}
