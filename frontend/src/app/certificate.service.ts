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
    currentAppointmentId:number = 0;

    constructor(private http: HttpClient, private router:Router) {}

    public login(username: any, password: any){
      return this.http.get<any>(this._url + 'login/' + username + '/' + password);
    }
  }