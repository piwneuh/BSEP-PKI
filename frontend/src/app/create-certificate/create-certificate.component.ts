import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Certificate } from '../certificate';
import { CertificateService } from '../certificate.service';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.css']
})
export class CreateCertificateComponent implements OnInit {

  newCertificate: Certificate = new Certificate()


  constructor(private _service: CertificateService, private router:Router) { }

  ngOnInit(): void {
  }

  createSertificate(){
    this._service.createCertificate(this.newCertificate).subscribe(() => this.router.navigate(['/admin']))
  }  

}
