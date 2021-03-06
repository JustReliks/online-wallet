import { Component, OnInit } from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {NavigationEnd, NavigationStart, Router} from "@angular/router";
import {filter} from "rxjs/operators";

@Component({
  selector: 'app-middle',
  templateUrl: './middle.component.html',
  styleUrls: ['./middle.component.scss'],
  animations: [trigger('routingAnimation', [
    state('*', style({opacity: 1})),

    transition('* => *', [
      style({opacity: 0}),
      animate('300ms 150ms ease-out',
        style({opacity: 1})),
    ]),

    transition(':leave', [
      style({
        display: 'block',
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        zIndex: 1000,
      }),
      animate('150ms ease-out',
        style({opacity: 0})),
    ]),
  ])]
})
export class MiddleComponent implements OnInit {
  public isMainPage: boolean;

  constructor(private router:Router) { router.events.pipe<NavigationStart>(filter<NavigationStart>(event => event instanceof NavigationEnd
  )).subscribe(res => {
    if (res.url === '/') {
      this.isMainPage = true;
    } else {
      this.isMainPage = false;
    }
  });}

  ngOnInit(): void {
  }

}
