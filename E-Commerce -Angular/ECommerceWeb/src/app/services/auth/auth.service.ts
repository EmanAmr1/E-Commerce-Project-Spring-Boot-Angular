import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, ObservableInput } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';


const BASIC_URL = "http://localhost:8083/"

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private userStorageService: UserStorageService) { }

  register(signupRequest: any): Observable<any> {
    return this.http.post(BASIC_URL + "sign-up", signupRequest);
  }


  login(username: string, password: string): any {
    //set Method: Sets the Content-Type header to application/json, specifying that the request body will be in JSON format.
    const headers = new HttpHeaders().set("Content-Type", "application/json");
    const body = { username, password };

    //observe: 'response': Tells Angular to return the entire HTTP response, not just the response body.
    return this.http.post(BASIC_URL + "authenticate", body, { headers, observe: 'response' }).pipe(
      map((res) => {

        //Extract Token: res.headers.get('authorization')?.substring(7)
        // extracts the JWT token from the Authorization header. The substring(7) removes the "Bearer " prefix.
        const token = res.headers.get('authorization')?.substring(7);

       //Extract User Data: const user = res.body retrieves the user object from the response body.
        const user = res.body;
        if (token && user) {
          this.userStorageService.saveToken(token);  //save token of this user
          this.userStorageService.saveUser(user);    //save user
          return true;
        }
        return false;
      }))
  }
/*
Observable: The result of this.http.post(...) is an observable that emits the HTTP response.
pipe Method: Takes one or more operators as arguments and returns a new observable 
             that applies the given operators to the values emitted by the source observable.
RxJS .map Operator
The map operator is used to transform the items emitted by an observable by applying a function to each item.
*/
}
