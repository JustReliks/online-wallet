import {Injectable, Type} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {filter} from 'rxjs/operators';

/**
 * application level message bus
 */
@Injectable()
export class ApplicationEventBroadcaster extends Subject<any> {

  onType<T>(messageClass: Type<T>): Observable<T> {
    return this.asObservable().pipe(filter(event => event instanceof messageClass));
  }

}
