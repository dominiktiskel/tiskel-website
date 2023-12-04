import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SalesLeadComponent } from './list/sales-lead.component';
import { SalesLeadDetailComponent } from './detail/sales-lead-detail.component';
import { SalesLeadUpdateComponent } from './update/sales-lead-update.component';
import SalesLeadResolve from './route/sales-lead-routing-resolve.service';

const salesLeadRoute: Routes = [
  {
    path: '',
    component: SalesLeadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalesLeadDetailComponent,
    resolve: {
      salesLead: SalesLeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalesLeadUpdateComponent,
    resolve: {
      salesLead: SalesLeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalesLeadUpdateComponent,
    resolve: {
      salesLead: SalesLeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default salesLeadRoute;
