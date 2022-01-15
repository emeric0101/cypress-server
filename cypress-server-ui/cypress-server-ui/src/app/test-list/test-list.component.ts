import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Test, TestServiceService} from "../test-service.service";

@Component({
  selector: 'app-test-list',
  templateUrl: './test-list.component.html',
  styleUrls: ['./test-list.component.css']
})
export class TestListComponent implements OnInit {
  tests: Test[] = [];

  @Output() onStart = new EventEmitter<Test>();

  constructor(
    private testService: TestServiceService
  ) { }

  async ngOnInit() {
    this.tests = await this.testService.getTests();
  }

  start(test: any) {
    this.onStart.emit(test);
  }
}
