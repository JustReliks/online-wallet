import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Currency} from "../entities/currency";

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
}
