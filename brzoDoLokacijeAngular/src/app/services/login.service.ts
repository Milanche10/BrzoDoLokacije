import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  public port : string = "http://147.91.204.115:10106";// http://147.91.204.115:10106 https://localhost:7219
  public loginurl : string = this.port + "/api/User/login";

  login(username: any, password: any) {
    return this.http.post<string>(this.loginurl,
      {
        "username": username,
        "password": password
      }
    )
  }
}
