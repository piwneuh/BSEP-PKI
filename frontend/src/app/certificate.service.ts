import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Content } from '@angular/compiler/src/render3/r3_ast';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root',
  })
  export class CertificateService {
    private _url = 'http://localhost:8081/';
    constructor(private http: HttpClient, private router:Router) {}

    public login(username: any, password: any){
      return this.http.get<any>(this._url + 'login/' + username + '/' + password);
    }

    public getCertificatesByUsername(){
      return this.http.get<any>(this._url + 'api/getAllByUsername' + '/' + localStorage.getItem('user'));
    }

    public getAllCertificates(){
      return this.http.get<any>(this._url + 'api/getAllCertificates');
    }

    public revokeCertificate(serialNumber: string){
      let revokeData = {
        serialNumber: serialNumber,
        revokeReason: 'ADMIN IS NOT HAPPY WITH THIS CERTIFICATE'
      }
      return this.http.post<any>(this._url + 'api/revokeCertificate', revokeData);
    }

    public getDetails(serialNumber: string){
      return this.http.get<any>(this._url + 'api/getCertificateDetails/' + serialNumber);
    }
  }