import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CertificateService } from '../certificate.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = "";
  password: string = "";
  user: any;

  constructor(private _service: CertificateService, private router:Router) { }

  ngOnInit(): void {
  }

  login(){
    this.user = this._service.login(this.username, this.password).subscribe(f => {this.user = f; this.setUser()})
    
  }

  setUser(){
    alert(this.user.role)
    localStorage.setItem('user', this.user.email);
    if (this.user.role === "ADMIN"){
      this.router.navigate(['/admin']);
    }
    else {
      this.router.navigate(['/client']);
    }
  }

}
