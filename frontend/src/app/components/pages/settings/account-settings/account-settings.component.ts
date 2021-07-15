import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-account-settings',
  templateUrl: './account-settings.component.html',
  styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {
  profileFormGroup: FormGroup;

  constructor() { }

  get controls() {
    return this.profileFormGroup.controls;
  }

  ngOnInit(): void {
    this.profileFormGroup = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      surName: new FormControl('', [Validators.required]),
      about: new FormControl('', []),
      url: new FormControl('', []),
      email: new FormControl('', [Validators.email]),
      phone: new FormControl('', []),
      country: new FormControl('', []),
      language: new FormControl('', [])
    });
  }

  hasControlsErrors(controlName: string, errorName: string) {
    return this.controls[controlName].hasError(errorName);
  }
}
