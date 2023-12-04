import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SalesLeadDetailComponent } from './sales-lead-detail.component';

describe('SalesLead Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalesLeadDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SalesLeadDetailComponent,
              resolve: { salesLead: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SalesLeadDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load salesLead on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SalesLeadDetailComponent);

      // THEN
      expect(instance.salesLead).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
