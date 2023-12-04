import dayjs from 'dayjs/esm';

import { ISalesLead, NewSalesLead } from './sales-lead.model';

export const sampleWithRequiredData: ISalesLead = {
  id: 26008,
};

export const sampleWithPartialData: ISalesLead = {
  id: 14554,
  created: dayjs('2023-12-04T01:20'),
};

export const sampleWithFullData: ISalesLead = {
  id: 4016,
  created: dayjs('2023-12-04T05:51'),
  phoneNumber: 'gadzooks ack boat',
  email: 'Esmeralda10@yahoo.com',
  note: 'under across alight',
  status: 'NEW',
};

export const sampleWithNewData: NewSalesLead = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
