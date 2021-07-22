import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit, AfterViewInit {

  @ViewChild('yaMetrika') yaMetrika: ElementRef;

  constructor(private renderer2: Renderer2) {
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    const srcScript = this.renderer2.createElement('script');
    srcScript.type = 'text/javascript';
    srcScript.text = `
           (function(m,e,t,r,i,k,a){m[i]=m[i]||function(){(m[i].a=m[i].a||[]).push(arguments)};
    m[i].l=1*new Date();k=e.createElement(t),a=e.getElementsByTagName(t)[0],k.async=1,k.src=r,a.parentNode.insertBefore(k,a)})
  (window, document, "script", "https://mc.yandex.ru/metrika/tag.js", "ym");

ym(83228629, "init", {
  clickmap:true,
  trackLinks:true,
  accurateTrackBounce:true
});
    `;
    this.renderer2.appendChild(this.yaMetrika.nativeElement, srcScript);
  }
}
