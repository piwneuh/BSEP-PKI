import { Component, OnInit } from '@angular/core';
import { CertificateService } from '../certificate.service';

@Component({
  selector: 'app-certificate-details',
  templateUrl: './certificate-details.component.html',
  styleUrls: ['./certificate-details.component.css']
})
export class CertificateDetailsComponent implements OnInit {
  certNum: any
  certificateData = {
    serialNumber: '',
    version: 0,
    signatureAlgorithm: '',
    signatureHashAlgorithm: '',
    publicKey: '',
    startDate: '',
    endDate: '',
    isRoot: false,
    subject: '',
    subjectGivenname: '',
    subjectSurname: '',
    subjectEmail: '',
    subjectCommonName: '',
    issuer: '',
    issuerGivenname: '',
    issuerSurname: '',
    issuerEmail: '',
    issuerCommonName: '',
    issuerSerialNumber: ''
  }
  constructor(private _service: CertificateService) { }

  ngOnInit(): void {
    this.getDetails();
  }

  getDetails(){
    this.certNum = localStorage.getItem('certificate');
    this._service.getDetails(this.certNum).subscribe(f => {
      this.certificateData = f;
    })
  }

}
