import {Component, OnInit} from '@angular/core';
import {CypressInstance, CypressService} from "./cypress.service";
import {Test, TestServiceService} from "./test-service.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  instances: CypressInstance[] = [];

  project: string = '';
  projects: any[] = [];

  tests: Test[] = [];
  testsForProject: Test[] = [];

  constructor(private cypressService: CypressService,
              private testService: TestServiceService
  ) {
    setInterval(() => this.fetchState(), 1000);
  }

  async ngOnInit() {
    this.tests = await this.testService.getTests();
    // extract project
    this.projects = [...new Set(this.tests.map(e => e.project))].map(e => ({label: e, value: e}));
    if (this.projects.length > 0) {
      if (!this.project) {
        this.project = this.projects[0].value;
      }
      this.onProjectChanged();
    }
  }

  async startAll() {
    await this.cypressService.startAll(this.project);
  }

  async stop() {
    await this.cypressService.stop(this.project);
  }

  async start($event: Test) {
    await this.cypressService.start(this.project, $event.name);
  }

  async fetchState() {
    if (!this.project) {
      return;
    }
    const state = await this.cypressService.getState(this.project);
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
    await this.cypressService.clear(this.project);
    this.instances = [];
    await this.ngOnInit();
  }

  onProjectChanged() {
    this.testsForProject = this.tests.filter(e => this.project != e.project);
  }

  async refresh() {
    await this.ngOnInit();
  }
}
