import {Injectable} from '@angular/core';
import {Account} from "../entities/account";
import {Observable} from "rxjs/Observable";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor() {
  }

  createAccount(account: Account): Observable<any> {
    return null;
  }
}
