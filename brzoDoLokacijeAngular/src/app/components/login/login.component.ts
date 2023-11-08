import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup} from '@angular/forms';
import { LoginService } from 'src/app/services/login.service';
import { Router, RouterLink } from '@angular/router';
import { HomeComponent } from '../home/home/home.component';
import { isNgContainer } from '@angular/compiler';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private loginService: LoginService, private router: Router) {
    this.loginForm = formBuilder.group({
      username: [''],
      password: ['']
    })
   }

  ngOnInit(): void {
  }

  login(form: FormGroup) {
    if (form.value.username && form.value.password) {

      if(form.value.username == "admin" && form.value.password == "admin")
      {
        sessionStorage.setItem("username", form.value.username)
        this.router.navigateByUrl('home');
      }
  }
}

}
