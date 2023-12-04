import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../demo-meeting.test-samples';

import { DemoMeetingFormService } from './demo-meeting-form.service';

describe('DemoMeeting Form Service', () => {
  let service: DemoMeetingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DemoMeetingFormService);
  });

  describe('Service methods', () => {
    describe('createDemoMeetingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDemoMeetingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            date: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });

      it('passing IDemoMeeting should create a new form with FormGroup', () => {
        const formGroup = service.createDemoMeetingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            date: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });
    });

    describe('getDemoMeeting', () => {
      it('should return NewDemoMeeting for default DemoMeeting initial value', () => {
        const formGroup = service.createDemoMeetingFormGroup(sampleWithNewData);

        const demoMeeting = service.getDemoMeeting(formGroup) as any;

        expect(demoMeeting).toMatchObject(sampleWithNewData);
      });

      it('should return NewDemoMeeting for empty DemoMeeting initial value', () => {
        const formGroup = service.createDemoMeetingFormGroup();

        const demoMeeting = service.getDemoMeeting(formGroup) as any;

        expect(demoMeeting).toMatchObject({});
      });

      it('should return IDemoMeeting', () => {
        const formGroup = service.createDemoMeetingFormGroup(sampleWithRequiredData);

        const demoMeeting = service.getDemoMeeting(formGroup) as any;

        expect(demoMeeting).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDemoMeeting should not enable id FormControl', () => {
        const formGroup = service.createDemoMeetingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDemoMeeting should disable id FormControl', () => {
        const formGroup = service.createDemoMeetingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
