import {Component, Input, OnInit} from '@angular/core';
import {AuthUser} from "../../../../entities/user";
import _ from "lodash";
import {TransactionHistoryService} from "../../../../service/transaction-history.service";
import {Transaction} from "../../../../entities/transaction";
import Handsontable from "handsontable";
import {AccountService} from "../../../../service/account.service";

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.scss']
})
export class TransactionHistoryComponent implements OnInit {
  private _currentUser: any;
  private _originalUser: any;
  private hot: Handsontable;

  @Input('user') set user(user: AuthUser) {
    this._originalUser = user;
    this.cloneOriginalUser();
  }

  get user() {
    return this._currentUser;
  }

  constructor(private _transactionHistoryService: TransactionHistoryService,
              private accountService: AccountService) {
    this.accountService.updateAccountsSubjectObservable.subscribe(res => {
      if (this.user != null) {
        this._transactionHistoryService.getTransactions(this.user?.id).subscribe(res => this.updateTable(res));
      }
    })
  }

  ngOnInit(): void {
    this._transactionHistoryService.getTransactions(this.user?.id).subscribe(res => this.prepareTable(res));
  }

  private prepareTable(res: Array<Transaction>) {
    let arr: any[] = [];
    _.forEach(res, r => {
      arr.push({
        account: r.account.name,
        category: r.category.title,
        datetime: r.dateTime,
        currency: r.accountBill.currency.shortName,
        sum: r.quantity
      })
    });

    const sumRenderer = function (instance, td, row, col, prop, value, cellProperties) {
      Handsontable.renderers.TextRenderer.apply(this, arguments);
      if (value > 0) {
        td.style.color = 'green';
      } else {
        td.style.color = 'red';
      }
    };

    const container = document.getElementById('transaction-table');
    this.hot = new Handsontable(container, {
      data: arr,
      colHeaders: ['Счёт', 'Категория', 'Время транзакции', 'Валюта', 'Сумма'],
      rowHeaders: true,
      height: 'auto',
      width: 'all',
      stretchH: 'all',
      filters: true,
      dropdownMenu: true,
      readOnly: true,
      contextMenu: true,
      language: "ru-RU",
      multiColumnSorting: true,
      licenseKey: "non-commercial-and-evaluation",
      columns: [
        {data: 'account'},
        {data: 'category'},
        {data: 'datetime'},
        {data: 'currency'},
        {data: 'sum', renderer: sumRenderer}
      ]
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

  private updateTable(res: Array<Transaction>) {
    let arr: any[] = [];
    _.forEach(res, r => {
      arr.push({
        account: r.account.name,
        category: r.category.title,
        datetime: r.dateTime,
        currency: r.accountBill.currency.shortName,
        sum: r.quantity
      })
    });
    this.hot.updateSettings({data: arr});
  }
}
