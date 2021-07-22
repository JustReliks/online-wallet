import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RestoreService {

  api = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  restoreAccessRequest(param: string): Observable<any> {
    return this.http.post<any>(this.api + `/restore-access?param=${param}`, null);
  }

  restoreAccessRequestGet(userId: string, action: string, token: string): Observable<any> {
    return this.http.get<any>(this.api + `/restore-access?userId=${userId}&action=${action}&token=${token}`);
  }
}
