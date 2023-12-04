import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalesLead } from '../sales-lead.model';
import { SalesLeadService } from '../service/sales-lead.service';

export const salesLeadResolve = (route: ActivatedRouteSnapshot): Observable<null | ISalesLead> => {
  const id = route.params['id'];
  if (id) {
    return inject(SalesLeadService)
      .find(id)
      .pipe(
        mergeMap((salesLead: HttpResponse<ISalesLead>) => {
          if (salesLead.body) {
            return of(salesLead.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default salesLeadResolve;
