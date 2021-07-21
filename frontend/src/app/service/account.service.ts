import {Injectable} from '@angular/core';
import {Account} from "../entities/account";
import {Observable} from "rxjs/Observable";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {AccountBill} from "../entities/account-bill";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  api = environment.apiUrl;
  private updateAccountsSubject = new BehaviorSubject({
    accounts: [],
  });
  updateAccountsSubjectObservable = this.updateAccountsSubject.asObservable();

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

  addTransaction(selectedBill: AccountBill, userId: number, value: any, isPlusState: boolean): Observable<AccountBill> {
    return this.http.post<AccountBill>(this.api + `/account/bill?userId=${userId}&plus=${isPlusState}&value=${value}`, selectedBill, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  getBalance(id: number, currencyToConvert: any): Observable<any> {
    return this.http.get<Array<any>>(this.api + `/account/balance?id=${id}&currency=${currencyToConvert}`);
  }

  updateAccountsEvent(event: any) {
    this.updateAccountsSubject.next(event)
  }
}
