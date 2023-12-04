import dayjs from 'dayjs/esm';

export interface IDemoMeeting {
  id: number;
  created?: dayjs.Dayjs | null;
  date?: dayjs.Dayjs | null;
  email?: string | null;
}

export type NewDemoMeeting = Omit<IDemoMeeting, 'id'> & { id: null };
