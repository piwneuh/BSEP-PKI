import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CertificateService } from '../certificate.service';

@Component({
  selector: 'app-admin-home-page',
  templateUrl: './admin-home-page.component.html',
  styleUrls: ['./admin-home-page.component.css']
})
export class AdminHomePageComponent implements OnInit {
  certificates: any;

  constructor(private _service:CertificateService, private router:Router) { }

  ngOnInit(): void {
    this.getAllCertificates();
  }

  getAllCertificates(){
    this._service.getAllCertificates().subscribe(f => {this.certificates = f;})
  }

  revokeCertificate(serialNumber: string){
    this._service.revokeCertificate(serialNumber).subscribe(()=>{this.getAllCertificates();})
  }

  createRedirect(){
    this.router.navigate(['/admin/create']);
  }

}
