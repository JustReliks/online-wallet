import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Account} from "../entities/account";
import {Transaction} from "../entities/transaction";

@Injectable({
  providedIn: 'root'
})
export class TransactionHistoryService {
  api = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getTransactions(userId: number): Observable<Array<Transaction>> {
    return this.http.get<Array<Transaction>>(this.api + `/transaction?userId=${userId}`);
  }
}
