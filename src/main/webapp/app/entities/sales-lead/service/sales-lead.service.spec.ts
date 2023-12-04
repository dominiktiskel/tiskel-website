import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISalesLead } from '../sales-lead.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sales-lead.test-samples';

import { SalesLeadService, RestSalesLead } from './sales-lead.service';

const requireRestSample: RestSalesLead = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.toJSON(),
};

describe('SalesLead Service', () => {
  let service: SalesLeadService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalesLead | ISalesLead[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalesLeadService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SalesLead', () => {
      const salesLead = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salesLead).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalesLead', () => {
      const salesLead = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salesLead).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalesLead', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalesLead', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SalesLead', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSalesLeadToCollectionIfMissing', () => {
      it('should add a SalesLead to an empty array', () => {
        const salesLead: ISalesLead = sampleWithRequiredData;
        expectedResult = service.addSalesLeadToCollectionIfMissing([], salesLead);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salesLead);
      });

      it('should not add a SalesLead to an array that contains it', () => {
        const salesLead: ISalesLead = sampleWithRequiredData;
        const salesLeadCollection: ISalesLead[] = [
          {
            ...salesLead,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalesLeadToCollectionIfMissing(salesLeadCollection, salesLead);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalesLead to an array that doesn't contain it", () => {
        const salesLead: ISalesLead = sampleWithRequiredData;
        const salesLeadCollection: ISalesLead[] = [sampleWithPartialData];
        expectedResult = service.addSalesLeadToCollectionIfMissing(salesLeadCollection, salesLead);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salesLead);
      });

      it('should add only unique SalesLead to an array', () => {
        const salesLeadArray: ISalesLead[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salesLeadCollection: ISalesLead[] = [sampleWithRequiredData];
        expectedResult = service.addSalesLeadToCollectionIfMissing(salesLeadCollection, ...salesLeadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salesLead: ISalesLead = sampleWithRequiredData;
        const salesLead2: ISalesLead = sampleWithPartialData;
        expectedResult = service.addSalesLeadToCollectionIfMissing([], salesLead, salesLead2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salesLead);
        expect(expectedResult).toContain(salesLead2);
      });

      it('should accept null and undefined values', () => {
        const salesLead: ISalesLead = sampleWithRequiredData;
        expectedResult = service.addSalesLeadToCollectionIfMissing([], null, salesLead, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salesLead);
      });

      it('should return initial array if no SalesLead is added', () => {
        const salesLeadCollection: ISalesLead[] = [sampleWithRequiredData];
        expectedResult = service.addSalesLeadToCollectionIfMissing(salesLeadCollection, undefined, null);
        expect(expectedResult).toEqual(salesLeadCollection);
      });
    });

    describe('compareSalesLead', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalesLead(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSalesLead(entity1, entity2);
        const compareResult2 = service.compareSalesLead(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSalesLead(entity1, entity2);
        const compareResult2 = service.compareSalesLead(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSalesLead(entity1, entity2);
        const compareResult2 = service.compareSalesLead(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
