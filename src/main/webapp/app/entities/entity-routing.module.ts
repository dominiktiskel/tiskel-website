import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'demo-meeting',
        data: { pageTitle: 'tiskelWebsiteApp.demoMeeting.home.title' },
        loadChildren: () => import('./demo-meeting/demo-meeting.routes'),
      },
      {
        path: 'sales-lead',
        data: { pageTitle: 'tiskelWebsiteApp.salesLead.home.title' },
        loadChildren: () => import('./sales-lead/sales-lead.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
