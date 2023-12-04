import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemoMeeting, NewDemoMeeting } from '../demo-meeting.model';

export type PartialUpdateDemoMeeting = Partial<IDemoMeeting> & Pick<IDemoMeeting, 'id'>;

type RestOf<T extends IDemoMeeting | NewDemoMeeting> = Omit<T, 'created' | 'date'> & {
  created?: string | null;
  date?: string | null;
};

export type RestDemoMeeting = RestOf<IDemoMeeting>;

export type NewRestDemoMeeting = RestOf<NewDemoMeeting>;

export type PartialUpdateRestDemoMeeting = RestOf<PartialUpdateDemoMeeting>;

export type EntityResponseType = HttpResponse<IDemoMeeting>;
export type EntityArrayResponseType = HttpResponse<IDemoMeeting[]>;

@Injectable({ providedIn: 'root' })
export class DemoMeetingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demo-meetings');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(demoMeeting: NewDemoMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demoMeeting);
    return this.http
      .post<RestDemoMeeting>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(demoMeeting: IDemoMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demoMeeting);
    return this.http
      .put<RestDemoMeeting>(`${this.resourceUrl}/${this.getDemoMeetingIdentifier(demoMeeting)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(demoMeeting: PartialUpdateDemoMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demoMeeting);
    return this.http
      .patch<RestDemoMeeting>(`${this.resourceUrl}/${this.getDemoMeetingIdentifier(demoMeeting)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDemoMeeting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDemoMeeting[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDemoMeetingIdentifier(demoMeeting: Pick<IDemoMeeting, 'id'>): number {
    return demoMeeting.id;
  }

  compareDemoMeeting(o1: Pick<IDemoMeeting, 'id'> | null, o2: Pick<IDemoMeeting, 'id'> | null): boolean {
    return o1 && o2 ? this.getDemoMeetingIdentifier(o1) === this.getDemoMeetingIdentifier(o2) : o1 === o2;
  }

  addDemoMeetingToCollectionIfMissing<Type extends Pick<IDemoMeeting, 'id'>>(
    demoMeetingCollection: Type[],
    ...demoMeetingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const demoMeetings: Type[] = demoMeetingsToCheck.filter(isPresent);
    if (demoMeetings.length > 0) {
      const demoMeetingCollectionIdentifiers = demoMeetingCollection.map(
        demoMeetingItem => this.getDemoMeetingIdentifier(demoMeetingItem)!,
      );
      const demoMeetingsToAdd = demoMeetings.filter(demoMeetingItem => {
        const demoMeetingIdentifier = this.getDemoMeetingIdentifier(demoMeetingItem);
        if (demoMeetingCollectionIdentifiers.includes(demoMeetingIdentifier)) {
          return false;
        }
        demoMeetingCollectionIdentifiers.push(demoMeetingIdentifier);
        return true;
      });
      return [...demoMeetingsToAdd, ...demoMeetingCollection];
    }
    return demoMeetingCollection;
  }

  protected convertDateFromClient<T extends IDemoMeeting | NewDemoMeeting | PartialUpdateDemoMeeting>(demoMeeting: T): RestOf<T> {
    return {
      ...demoMeeting,
      created: demoMeeting.created?.toJSON() ?? null,
      date: demoMeeting.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDemoMeeting: RestDemoMeeting): IDemoMeeting {
    return {
      ...restDemoMeeting,
      created: restDemoMeeting.created ? dayjs(restDemoMeeting.created) : undefined,
      date: restDemoMeeting.date ? dayjs(restDemoMeeting.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDemoMeeting>): HttpResponse<IDemoMeeting> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDemoMeeting[]>): HttpResponse<IDemoMeeting[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
