import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/observable';
import {map} from 'rxjs/operators';
import {environment} from "../../environments/environment";
import {AuthUser, UserLight} from "../entities/user";
import {UserSettings} from "../entities/user-settings";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  api = environment.apiUrl;

  constructor(private http: HttpClient) {
  }


  public register(userRegister: any, isGenerateDemoAccounts: boolean): Observable<number> {
    return this.http.post<number>(this.api + `/registration?generate-demo=${isGenerateDemoAccounts}`, userRegister, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  public checkUserExistByUsername(username: string): Observable<boolean> {
    return this.http.get<boolean>(this.api + `/registration/exist?username=${username}`);
  }

  public checkUserExistByEmail(email: string): Observable<boolean> {
    return this.http.get<boolean>(this.api + `/registration/exist?email=${email}`);
  }

  public getUserRights(username: string): Observable<any> {
    return this.http.get<any>(this.api + `/user/${username}/rights`);
  }

  public changePassword(userId: number, oldPass: string, newPass: string, confirmNewPass: string): Observable<AuthUser> {
    return this.http.put<AuthUser>(this.api + `/user/password`, {
      userId,
      oldPass: btoa(oldPass),
      newPass: btoa(newPass),
      confirmNewPass: btoa(confirmNewPass)
    })
  }

  public generateGoogleAuthKey(username: string, regenerate?: boolean): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
    const options = {
      headers, responseType: 'text'
    }
    return this.http.get<any>(this.api + `/code/generate/${username}${regenerate ? '?regenerate=true' : ''}`,
      options as any).pipe(
      map((result: HttpResponse<any>) => {
        return result;
      }));
  }

  public validateKey(username: string, code: string): Observable<boolean> {
    return this.http.post<boolean>(this.api + `/code/validate/key`, {
      username,
      code
    });
  }

  public changeTwoFactorState(username: string, action: string): Observable<UserLight> {
    return this.http.get<UserLight>(this.api + `/code/two-factor/${username}?action=${action}`);
  }

  public getUserSettings(id: number) {
    return this.http.get<any>(this.api + `/user/settings?id=${id}`);
  }

  updateUserProfile(userProfile: UserSettings) {
    return this.http.put<UserSettings>(this.api + `/user/settings`, userProfile
    )
  }
}
