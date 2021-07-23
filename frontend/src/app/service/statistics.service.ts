import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Statistics} from "../entities/statistic/statistic";

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  api = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getAccountStatistic(accountId: number, days:number): Observable<Statistics> {
    return this.http.get<Statistics>(this.api + `/statistics?account=${accountId}&days=${days}`);
  }
}
