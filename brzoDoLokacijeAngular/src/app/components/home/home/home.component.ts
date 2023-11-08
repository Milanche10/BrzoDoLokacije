import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  download() {
    console.log("download")

    let link = document.createElement('a');
    link.download = 'BrzoDoLokacije';
    link.href = 'assets/app-debug.apk';
    link.click()
  }

  logout() {
    console.log("logout")

    if(sessionStorage.getItem("username"))
    {
      sessionStorage.removeItem("username")
      this.router.navigateByUrl('');
    }
  }

}
