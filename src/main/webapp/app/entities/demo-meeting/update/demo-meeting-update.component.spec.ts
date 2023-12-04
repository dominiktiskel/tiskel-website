import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemoMeetingService } from '../service/demo-meeting.service';
import { IDemoMeeting } from '../demo-meeting.model';
import { DemoMeetingFormService } from './demo-meeting-form.service';

import { DemoMeetingUpdateComponent } from './demo-meeting-update.component';

describe('DemoMeeting Management Update Component', () => {
  let comp: DemoMeetingUpdateComponent;
  let fixture: ComponentFixture<DemoMeetingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demoMeetingFormService: DemoMeetingFormService;
  let demoMeetingService: DemoMeetingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DemoMeetingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DemoMeetingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemoMeetingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demoMeetingFormService = TestBed.inject(DemoMeetingFormService);
    demoMeetingService = TestBed.inject(DemoMeetingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const demoMeeting: IDemoMeeting = { id: 456 };

      activatedRoute.data = of({ demoMeeting });
      comp.ngOnInit();

      expect(comp.demoMeeting).toEqual(demoMeeting);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemoMeeting>>();
      const demoMeeting = { id: 123 };
      jest.spyOn(demoMeetingFormService, 'getDemoMeeting').mockReturnValue(demoMeeting);
      jest.spyOn(demoMeetingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demoMeeting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demoMeeting }));
      saveSubject.complete();

      // THEN
      expect(demoMeetingFormService.getDemoMeeting).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(demoMeetingService.update).toHaveBeenCalledWith(expect.objectContaining(demoMeeting));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemoMeeting>>();
      const demoMeeting = { id: 123 };
      jest.spyOn(demoMeetingFormService, 'getDemoMeeting').mockReturnValue({ id: null });
      jest.spyOn(demoMeetingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demoMeeting: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demoMeeting }));
      saveSubject.complete();

      // THEN
      expect(demoMeetingFormService.getDemoMeeting).toHaveBeenCalled();
      expect(demoMeetingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemoMeeting>>();
      const demoMeeting = { id: 123 };
      jest.spyOn(demoMeetingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demoMeeting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demoMeetingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
