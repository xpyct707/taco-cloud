import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'little-button',
  templateUrl: './little-button.component.html',
  styleUrls: ['./little-button.component.css']
})
export class LittleButtonComponent implements OnInit {

  @Input() label: String;

  constructor() { }

  ngOnInit() {
  }

}
