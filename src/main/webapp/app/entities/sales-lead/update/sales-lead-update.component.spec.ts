import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SalesLeadService } from '../service/sales-lead.service';
import { ISalesLead } from '../sales-lead.model';
import { SalesLeadFormService } from './sales-lead-form.service';

import { SalesLeadUpdateComponent } from './sales-lead-update.component';

describe('SalesLead Management Update Component', () => {
  let comp: SalesLeadUpdateComponent;
  let fixture: ComponentFixture<SalesLeadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salesLeadFormService: SalesLeadFormService;
  let salesLeadService: SalesLeadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SalesLeadUpdateComponent],
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
      .overrideTemplate(SalesLeadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalesLeadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salesLeadFormService = TestBed.inject(SalesLeadFormService);
    salesLeadService = TestBed.inject(SalesLeadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const salesLead: ISalesLead = { id: 456 };

      activatedRoute.data = of({ salesLead });
      comp.ngOnInit();

      expect(comp.salesLead).toEqual(salesLead);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalesLead>>();
      const salesLead = { id: 123 };
      jest.spyOn(salesLeadFormService, 'getSalesLead').mockReturnValue(salesLead);
      jest.spyOn(salesLeadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salesLead });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salesLead }));
      saveSubject.complete();

      // THEN
      expect(salesLeadFormService.getSalesLead).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salesLeadService.update).toHaveBeenCalledWith(expect.objectContaining(salesLead));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalesLead>>();
      const salesLead = { id: 123 };
      jest.spyOn(salesLeadFormService, 'getSalesLead').mockReturnValue({ id: null });
      jest.spyOn(salesLeadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salesLead: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salesLead }));
      saveSubject.complete();

      // THEN
      expect(salesLeadFormService.getSalesLead).toHaveBeenCalled();
      expect(salesLeadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalesLead>>();
      const salesLead = { id: 123 };
      jest.spyOn(salesLeadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salesLead });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salesLeadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
