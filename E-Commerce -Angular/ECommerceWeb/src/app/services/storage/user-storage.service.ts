import { Injectable } from '@angular/core';


const TOKEN="ecom-token"
const User="ecom-user"

@Injectable({
  providedIn: 'root'
})


export class UserStorageService {

  constructor() { }


  public saveToken(token: string) :void{
    window.localStorage.removeItem(TOKEN);//remove the existing token
    window.localStorage.setItem(TOKEN,token);
  }


  public saveUser(user: any) :void{
    window.localStorage.removeItem(User); //remove the existing user
    window.localStorage.setItem(TOKEN,JSON.stringify(user));
  }



}
