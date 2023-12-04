import dayjs from 'dayjs/esm';
import { SalesLeadStatus } from 'app/entities/enumerations/sales-lead-status.model';

export interface ISalesLead {
  id: number;
  created?: dayjs.Dayjs | null;
  phoneNumber?: string | null;
  email?: string | null;
  note?: string | null;
  status?: keyof typeof SalesLeadStatus | null;
}

export type NewSalesLead = Omit<ISalesLead, 'id'> & { id: null };
