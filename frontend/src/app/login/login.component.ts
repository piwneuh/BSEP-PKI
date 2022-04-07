import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = "";
  password: string = "";

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  login(){
    alert(this.username + this.password)
    let loginForm = {
      username: this.username,
      password: this.password,
    };
    return this.http.post<any>("http://localhost:8081/" + 'login/login', loginForm);
  }

}
