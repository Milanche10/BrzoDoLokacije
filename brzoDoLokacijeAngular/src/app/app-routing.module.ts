import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuardService } from './services/auth-guard.service';

const routes: Routes = [
  {
    path:"",
    component: LoginComponent,
    pathMatch:'full'
  },
  {
    path:"home",
    component: HomeComponent,
    canActivate:[AuthGuardService]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
