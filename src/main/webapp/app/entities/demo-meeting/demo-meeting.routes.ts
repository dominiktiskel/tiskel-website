import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DemoMeetingComponent } from './list/demo-meeting.component';
import { DemoMeetingDetailComponent } from './detail/demo-meeting-detail.component';
import { DemoMeetingUpdateComponent } from './update/demo-meeting-update.component';
import DemoMeetingResolve from './route/demo-meeting-routing-resolve.service';

const demoMeetingRoute: Routes = [
  {
    path: '',
    component: DemoMeetingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemoMeetingDetailComponent,
    resolve: {
      demoMeeting: DemoMeetingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemoMeetingUpdateComponent,
    resolve: {
      demoMeeting: DemoMeetingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemoMeetingUpdateComponent,
    resolve: {
      demoMeeting: DemoMeetingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default demoMeetingRoute;
