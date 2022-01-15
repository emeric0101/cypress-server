import { Component } from '@angular/core';
import {CypressInstance, CypressService} from "./cypress.service";
import {Test} from "./test-service.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  instances: CypressInstance[] = [];

  constructor(private cypressService: CypressService) {
    setInterval(() => this.fetchState(), 1000);
  }

  async startAll() {
    await this.cypressService.startAll();
  }

  async stop() {
    await this.cypressService.stop();
  }

  async start($event: Test) {
    await this.cypressService.start($event.name);
  }

  async fetchState() {
    const state = await this.cypressService.getState();
    for (var instance of state.instances) {
      const currentInstance = this.instances.find(e => e.id == instance.id);
      if (currentInstance) {
        currentInstance.result = instance.result;
        currentInstance.failure = instance.failure;
        currentInstance.running = instance.running;
        currentInstance.ended = instance.ended;
      } else {
        this.instances = [instance, ...this.instances];
      }
    }
  }

  async clear() {
    await this.cypressService.clear();

  }
}
