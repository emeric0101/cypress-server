import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {lastValueFrom} from "rxjs";
import {environment} from "../environments/environment";
import {State} from "./cypress.service";


export interface Test {
  name: string;
  project: string;
}

@Injectable({
  providedIn: 'root'
})
export class TestServiceService {
  constructor(
    private httpClient: HttpClient
  ) { }

  public getTests() {
    return lastValueFrom(this.httpClient.get<Test[]>(environment.serverUrl + "/test"));
  }
}
