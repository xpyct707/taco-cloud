import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'big-button',
  templateUrl: './big-button.component.html',
  styleUrls: ['./big-button.component.css']
})

export class BigButtonComponent implements OnInit {

  @Input() label: String;

  constructor() { }

  ngOnInit() { }
}
