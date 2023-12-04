import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemoMeeting } from '../demo-meeting.model';
import { DemoMeetingService } from '../service/demo-meeting.service';

export const demoMeetingResolve = (route: ActivatedRouteSnapshot): Observable<null | IDemoMeeting> => {
  const id = route.params['id'];
  if (id) {
    return inject(DemoMeetingService)
      .find(id)
      .pipe(
        mergeMap((demoMeeting: HttpResponse<IDemoMeeting>) => {
          if (demoMeeting.body) {
            return of(demoMeeting.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default demoMeetingResolve;
