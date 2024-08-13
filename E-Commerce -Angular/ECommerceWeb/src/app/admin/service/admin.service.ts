import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';

const BASIC_URL="http://localhost:8083/"

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  addCategory(categoryDto:any): Observable<any>{
    return this.http.post(BASIC_URL + 'api/admin/category',categoryDto
      ,{headers:this.createAuthorizationHeader()});

  }


      private createAuthorizationHeader(): HttpHeaders {
        const token = UserStorageService.getToken(); 
        console.log('Generated Token:', token);
        return new HttpHeaders().set('Authorization', `Bearer ${token}`);
      }

}
