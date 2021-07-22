import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Currency} from "../entities/currency";
import {Type} from "../entities/type";
import {TransactionCategory} from "../entities/transaction-category";

@Injectable({
  providedIn: 'root'
})
export class DictionaryService {
  api = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getAllCurrencies(): Observable<Array<Currency>> {
    return this.http.get<Array<Currency>>(this.api + '/dictionary/currencies'
    );
  }

  getAllAccountTypes(): Observable<Array<Type>> {
    return this.http.get<Array<Type>>(this.api + '/dictionary/account-types');
  }

  getAllTransactionCategories(): Observable<Array<TransactionCategory>> {
    return this.http.get<Array<TransactionCategory>>(this.api + '/dictionary/transaction-categories');
  }
}
