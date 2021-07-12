import {NgModule} from '@angular/core';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatCardModule} from '@angular/material/card';
import {MatMenuModule} from '@angular/material/menu';
import {MatIconModule} from '@angular/material/icon';
import {MatRadioModule} from '@angular/material/radio';
import {MatSliderModule} from '@angular/material/slider';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatTableModule} from '@angular/material/table';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

@NgModule({
  imports: [MatButtonModule, MatCheckboxModule, MatToolbarModule, MatInputModule,
    MatCardModule, MatMenuModule, MatIconModule, MatSliderModule, MatTooltipModule, MatPaginatorModule, MatProgressSpinnerModule, MatFormFieldModule, MatCardModule],
  exports: [MatDialogModule, MatFormFieldModule, MatButtonModule, MatInputModule, MatRadioModule,
    MatSliderModule, MatTableModule, MatSlideToggleModule, MatProgressSpinnerModule, MatIconModule, MatCardModule]
})
export class MaterialModule {
}
