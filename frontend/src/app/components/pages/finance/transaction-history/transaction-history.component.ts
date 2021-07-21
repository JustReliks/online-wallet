import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Account} from "../../../../entities/account";
import {AuthUser} from "../../../../entities/user";
import _ from "lodash";
import {TransactionHistoryService} from "../../../../service/transaction-history.service";
import {Transaction} from "../../../../entities/transaction";
import {HotTableComponent} from "@handsontable/angular";
import Handsontable from "handsontable";

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.scss']
})
export class TransactionHistoryComponent implements OnInit {
  private _currentUser: any;
  private _originalUser: any;
  private _accounts: Array<Account>;
  dataset: any[] = [];
  @ViewChild("hot", {static: false}) hot: HotTableComponent;

  settings: Handsontable.GridSettings = {
    data: []
  }

  @Input('user') set user(user: AuthUser) {
    console.log(user)
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  get user() {
    return this._currentUser;
  }

  constructor(private _transactionHistoryService: TransactionHistoryService) {
  }

  ngOnInit(): void {
    this._transactionHistoryService.getTransactions(this.user?.id).subscribe(res => this.prepareTableDataSet(res));
  }

  private prepareTableDataSet(res: Array<Transaction>) {
    let arr: any[] = [];
    _.forEach(res, r => {
      arr.push({
        id: r.account.id,
        account: r.account.name,
        category: r.categoryId,
        datetime: r.dateTime,
        sum: r.quantity
      })
    });
    this.settings.data = arr;
    this.hot.updateHotTable(this.settings);
    console.log(this.dataset)
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }

}
