import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';

const BASIC_URL =  "http://localhost:8080/";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private http: HttpClient,
    private userStorageService: UserStorageService
  ) { }

  register(signupRequest: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
  
    
    return this.http.post(BASIC_URL + "sign-up", signupRequest, { headers });
  }
  
  login(username: string, password: string): any {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '
  });
    const body = {username, password};

    return this.http.post(BASIC_URL + "authenticate", body, { headers, observe: 'response' }).pipe(
      map((res) => {
        const token = res.headers.get('Authorization').substring(7);

        const user = res.body;

        if(token && user){
          this.userStorageService.saveToken(token);
          this.userStorageService.saveUser(user);

          return true;
        }
        return false;
      })
    )
  }
  
}
