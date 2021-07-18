import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent implements OnInit {

  accountNameFilled: boolean

  constructor(private dialogRef:MatDialogRef<any>) { }

  ngOnInit(): void {
  }

  changeMainCurrency() {

  }

  close() {
    this.dialogRef.close();
  }
}
