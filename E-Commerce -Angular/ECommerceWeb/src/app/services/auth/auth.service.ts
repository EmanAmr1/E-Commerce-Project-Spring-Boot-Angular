import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, ObservableInput } from 'rxjs';


const BASIC_URL="http://localhost:8083/"

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http:HttpClient) { }

  register(signupRequest:any):Observable<any>{
   return this.http.post(BASIC_URL + "sign-up" ,signupRequest);
  }
}
