import {Injectable} from '@angular/core';
import {Account} from "../entities/account";
import {Observable} from "rxjs/Observable";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {AccountBill} from "../entities/account-bill";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  api = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  createAccount(account: Account): Observable<any> {
    return this.http.post<number>(this.api + '/account', account, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  getAccounts(id: number): Observable<Array<Account>> {
    return this.http.get<Array<Account>>(this.api + `/account?id=${id}`);
  }

  addTransaction(selectedBill: AccountBill, value: any, isPlusState: boolean): Observable<AccountBill> {
    return this.http.post<AccountBill>(this.api + `/account/bill?plus=${isPlusState}&value=${value}`, selectedBill, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }
}
