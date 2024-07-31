import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserStorageService } from '../services/storage/user-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm !: FormGroup;

  hidepassword = true;

  constructor(private fb: FormBuilder, private snackBar: MatSnackBar, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {

    this.loginForm = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
    })
  }



  togglePasswordVisibility() {
    this.hidepassword = !this.hidepassword
  }

  onSubmit(): void {

    const userName = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;



    this.authService.login(userName, password).subscribe(


      (response: any) => {

        console.log('Token:', UserStorageService.getToken());
        console.log('Role:', UserStorageService.getUserRole());

        if(UserStorageService.isAdminLoggedIn()){
          this.router.navigateByUrl("/admin/dashboard");
        } else if(UserStorageService.isCustomerLoggedIn()){
          this.router.navigateByUrl("/customer/dashboard");
        }


      }

      , (error: HttpErrorResponse) => {
        this.snackBar.open("Bad credentials, please try again", "Error", { duration: 5000 });

      }

    )
  }

}
