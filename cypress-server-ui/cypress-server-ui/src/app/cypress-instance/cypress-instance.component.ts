import {Component, Input, OnInit} from '@angular/core';
import {CypressInstance} from "../cypress.service";

@Component({
  selector: 'app-cypress-instance',
  templateUrl: './cypress-instance.component.html',
  styleUrls: ['./cypress-instance.component.css']
})
export class CypressInstanceComponent implements OnInit {

  @Input() instance: CypressInstance | null = null;
  @Input() project: string | null = null;
  constructor() { }

  ngOnInit(): void {
  }

}
