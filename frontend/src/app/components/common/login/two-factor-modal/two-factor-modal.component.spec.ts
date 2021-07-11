import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TwoFactorModalComponent } from './two-factor-modal.component';

describe('TwoFactorModalComponent', () => {
  let component: TwoFactorModalComponent;
  let fixture: ComponentFixture<TwoFactorModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TwoFactorModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TwoFactorModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
