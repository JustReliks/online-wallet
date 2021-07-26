import {Component, Input, OnInit} from '@angular/core';
import {Type} from "../../../../../entities/type";
import {DomSanitizer} from "@angular/platform-browser";
import {AccountService} from "../../../../../service/account.service";

@Component({
  selector: 'app-type-info',
  templateUrl: './type-info.component.html',
  styleUrls: ['./type-info.component.scss']
})
export class TypeInfoComponent implements OnInit {

  @Input() public types: Array<Type>;

  constructor(private accountService: AccountService,
              private _sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
  }

  getTypeIcon(type: Type) {
    return this._sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${type?.icon}`);
  }

  returnBack() {
    this.accountService.changeTypeInfoSubject.next({});
  }
}
