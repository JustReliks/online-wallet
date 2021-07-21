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
  private hot: Handsontable;

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
    this._transactionHistoryService.getTransactions(this.user?.id).subscribe(res => this.prepareTable(res));
  }

  private prepareTable(res: Array<Transaction>) {
    let arr: any[] = [];
    _.forEach(res, r => {
      arr.push({
        id: r.account.id,
        account: r.account.name,
        category: r.categoryId,
        datetime: r.dateTime,
        currency: r.accountBill.currency.shortName,
        sum: r.quantity
      })
    });

    const container = document.getElementById('transaction-table');
    this.hot = new Handsontable(container, {
      data: arr,
      colHeaders: ['ID', 'Счёт', 'Категория', 'Время транзакции', 'Валюта', 'Сумма'],
      rowHeaders: true,
      height: 'auto',
      width: 'all',
      stretchH: 'all',
      filters: true,
      dropdownMenu: true,
      readOnly:true,
      contextMenu:true,
      language: "ru-RU",
      licenseKey: "non-commercial-and-evaluation"
    });
  }

  export() {
    let plugin = this.hot.getPlugin("exportFile");
    plugin.downloadFile('csv', {
      bom: false,
      columnDelimiter: ',',
      columnHeaders: false,
      exportHiddenColumns: true,
      exportHiddenRows: true,
      fileExtension: 'csv',
      filename: 'Transactions-history_[YYYY]-[MM]-[DD]',
      mimeType: 'text/csv',
      rowDelimiter: '\r\n',
      rowHeaders: true
    });
  }

  cloneOriginalUser() {
    this._currentUser = _.cloneDeep(this._originalUser);
  }

}
