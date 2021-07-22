import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RestoreAccessComponent } from './restore-access.component';

describe('RestoreAccessComponent', () => {
  let component: RestoreAccessComponent;
  let fixture: ComponentFixture<RestoreAccessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RestoreAccessComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestoreAccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
