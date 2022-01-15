import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";
import {lastValueFrom} from "rxjs";



export interface CypressInstance {
  id: number;
  ended: string;
  failure: boolean;
  result: string[];
  running: boolean;
  started: string;
  test: string;
}
export interface State {
  instances: CypressInstance[];
}

@Injectable({
  providedIn: 'root'
})
export class CypressService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public getState() {
    return lastValueFrom(this.httpClient.get<State>(environment.serverUrl + "/cypress/state"));
  }

  public startAll() {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/cypress/start/all"));
  }

  public start(test: string) {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/cypress/start/" + test));
  }
  public stop() {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/cypress/stop"));
  }

  async clear() {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/cypress/clear"));
  }
}
