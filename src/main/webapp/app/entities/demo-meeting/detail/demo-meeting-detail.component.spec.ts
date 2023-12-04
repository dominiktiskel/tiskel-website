import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DemoMeetingDetailComponent } from './demo-meeting-detail.component';

describe('DemoMeeting Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemoMeetingDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DemoMeetingDetailComponent,
              resolve: { demoMeeting: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DemoMeetingDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load demoMeeting on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DemoMeetingDetailComponent);

      // THEN
      expect(instance.demoMeeting).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
