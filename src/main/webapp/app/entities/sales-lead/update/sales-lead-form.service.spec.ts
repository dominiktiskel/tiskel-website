import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sales-lead.test-samples';

import { SalesLeadFormService } from './sales-lead-form.service';

describe('SalesLead Form Service', () => {
  let service: SalesLeadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalesLeadFormService);
  });

  describe('Service methods', () => {
    describe('createSalesLeadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalesLeadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            phoneNumber: expect.any(Object),
            email: expect.any(Object),
            note: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing ISalesLead should create a new form with FormGroup', () => {
        const formGroup = service.createSalesLeadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            phoneNumber: expect.any(Object),
            email: expect.any(Object),
            note: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getSalesLead', () => {
      it('should return NewSalesLead for default SalesLead initial value', () => {
        const formGroup = service.createSalesLeadFormGroup(sampleWithNewData);

        const salesLead = service.getSalesLead(formGroup) as any;

        expect(salesLead).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalesLead for empty SalesLead initial value', () => {
        const formGroup = service.createSalesLeadFormGroup();

        const salesLead = service.getSalesLead(formGroup) as any;

        expect(salesLead).toMatchObject({});
      });

      it('should return ISalesLead', () => {
        const formGroup = service.createSalesLeadFormGroup(sampleWithRequiredData);

        const salesLead = service.getSalesLead(formGroup) as any;

        expect(salesLead).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalesLead should not enable id FormControl', () => {
        const formGroup = service.createSalesLeadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalesLead should disable id FormControl', () => {
        const formGroup = service.createSalesLeadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
