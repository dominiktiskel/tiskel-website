import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalesLead, NewSalesLead } from '../sales-lead.model';

export type PartialUpdateSalesLead = Partial<ISalesLead> & Pick<ISalesLead, 'id'>;

type RestOf<T extends ISalesLead | NewSalesLead> = Omit<T, 'created'> & {
  created?: string | null;
};

export type RestSalesLead = RestOf<ISalesLead>;

export type NewRestSalesLead = RestOf<NewSalesLead>;

export type PartialUpdateRestSalesLead = RestOf<PartialUpdateSalesLead>;

export type EntityResponseType = HttpResponse<ISalesLead>;
export type EntityArrayResponseType = HttpResponse<ISalesLead[]>;

@Injectable({ providedIn: 'root' })
export class SalesLeadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sales-leads');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(salesLead: NewSalesLead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salesLead);
    return this.http
      .post<RestSalesLead>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(salesLead: ISalesLead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salesLead);
    return this.http
      .put<RestSalesLead>(`${this.resourceUrl}/${this.getSalesLeadIdentifier(salesLead)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(salesLead: PartialUpdateSalesLead): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salesLead);
    return this.http
      .patch<RestSalesLead>(`${this.resourceUrl}/${this.getSalesLeadIdentifier(salesLead)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSalesLead>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSalesLead[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSalesLeadIdentifier(salesLead: Pick<ISalesLead, 'id'>): number {
    return salesLead.id;
  }

  compareSalesLead(o1: Pick<ISalesLead, 'id'> | null, o2: Pick<ISalesLead, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalesLeadIdentifier(o1) === this.getSalesLeadIdentifier(o2) : o1 === o2;
  }

  addSalesLeadToCollectionIfMissing<Type extends Pick<ISalesLead, 'id'>>(
    salesLeadCollection: Type[],
    ...salesLeadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salesLeads: Type[] = salesLeadsToCheck.filter(isPresent);
    if (salesLeads.length > 0) {
      const salesLeadCollectionIdentifiers = salesLeadCollection.map(salesLeadItem => this.getSalesLeadIdentifier(salesLeadItem)!);
      const salesLeadsToAdd = salesLeads.filter(salesLeadItem => {
        const salesLeadIdentifier = this.getSalesLeadIdentifier(salesLeadItem);
        if (salesLeadCollectionIdentifiers.includes(salesLeadIdentifier)) {
          return false;
        }
        salesLeadCollectionIdentifiers.push(salesLeadIdentifier);
        return true;
      });
      return [...salesLeadsToAdd, ...salesLeadCollection];
    }
    return salesLeadCollection;
  }

  protected convertDateFromClient<T extends ISalesLead | NewSalesLead | PartialUpdateSalesLead>(salesLead: T): RestOf<T> {
    return {
      ...salesLead,
      created: salesLead.created?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSalesLead: RestSalesLead): ISalesLead {
    return {
      ...restSalesLead,
      created: restSalesLead.created ? dayjs(restSalesLead.created) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSalesLead>): HttpResponse<ISalesLead> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSalesLead[]>): HttpResponse<ISalesLead[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
