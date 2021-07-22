import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountsStatisticComponent } from './accounts-statistic.component';

describe('AccountsStatisticComponent', () => {
  let component: AccountsStatisticComponent;
  let fixture: ComponentFixture<AccountsStatisticComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountsStatisticComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountsStatisticComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
