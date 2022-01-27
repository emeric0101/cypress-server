import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TestListComponent } from './test-list/test-list.component';
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import { CypressInstanceComponent } from './cypress-instance/cypress-instance.component';
import {AccordionModule} from "primeng/accordion";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    TestListComponent,
    CypressInstanceComponent
  ],
    imports: [
        BrowserModule,
        FormsModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        TableModule,
        ButtonModule,
        AccordionModule,
        HttpClientModule,
        DropdownModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
