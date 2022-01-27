import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Test, TestServiceService} from "../test-service.service";

@Component({
  selector: 'app-test-list',
  templateUrl: './test-list.component.html',
  styleUrls: ['./test-list.component.css']
})
export class TestListComponent implements OnInit {
  @Input() tests: Test[] = [];

  @Output() onStart = new EventEmitter<Test>();

  constructor(
  ) { }

  async ngOnInit() {
  }

  start(test: any) {
    this.onStart.emit(test);
  }
}
