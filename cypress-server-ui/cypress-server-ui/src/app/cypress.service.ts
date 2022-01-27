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

  public getState(project: string) {
    return lastValueFrom(this.httpClient.get<State>(environment.serverUrl + "/cypress/" + project + "/state"));
  }

  public startAll(project: string) {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/cypress/" + project + "/start/all"));
  }

  public start(project: string, test: string) {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/" + project + "/cypress/start/" + test));
  }
  public stop(project: string) {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/" + project + "/cypress/stop"));
  }

  async clear(project: string) {
    return lastValueFrom(this.httpClient.get(environment.serverUrl + "/" + project + "/cypress/clear"));
  }


}
