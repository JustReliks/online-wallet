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
    updateStatistics: false
  });
  changeTypeInfoSubject = new BehaviorSubject({});

  updateTypeInfoObservable = this.changeTypeInfoSubject.asObservable();
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

  addTransaction(selectedBill: AccountBill, userId: number, value: any, isPlusState: boolean, categoryId: number): Observable<AccountBill> {
    return this.http.post<AccountBill>(this.api + `/account/bill?userId=${userId}&plus=${isPlusState}&value=${value}&category=${categoryId}`, selectedBill, {
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

  updateAccount(account: Account): Observable<Account> {
    return this.http.put<Account>(this.api + '/account', account, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  deleteAccount(account: Account) {
    return this.http.delete<Account>(this.api + `/account?id=${account.id}`);
  }

  //    return this.http.delete<boolean>(this.api + '/account', account, {
  //       headers: {
  //         'Content-Type': 'application/json'
  //       }
  //     });
  convertCurrencies(convertFromValue: any, convertToValue: any, value: any) {
    return this.http.get<Array<any>>(this.api + `/account/convert?from=${convertFromValue}&to=${convertToValue}&value=${value}`);
  }

  getAccountsCount(id: number): Observable<number> {
    return this.http.get<number>(this.api + `/account/count?userId=${id}`);
  }
}
