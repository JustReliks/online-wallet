import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs/observable";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private api = environment.apiUrl;

  private changeProfileImageSubject = new BehaviorSubject({
    source: '',
    state: 'new'
  });
  changeProfileImageSubjectObservable = this.changeProfileImageSubject.asObservable();

  constructor(private https: HttpClient) {
  }

  saveProfileImage(userId: number, uploadFile: File): Observable<any> {
    if (!uploadFile) {
      return null;
    }
    const data = this.getData(uploadFile, userId.toString());
    return this.https.post(this.api + '/user/settings', data, {observe: 'response'});
  }

  changeProfileImage(event: any) {
    this.changeProfileImageSubject.next(event)
  }

  private getData(uploadFile: File, userId: string) {
    const data: FormData = new FormData();
    data.append('file', uploadFile);
    data.append('userId', userId);
    return data;
  }
}
