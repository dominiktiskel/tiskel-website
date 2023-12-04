import dayjs from 'dayjs/esm';

import { IDemoMeeting, NewDemoMeeting } from './demo-meeting.model';

export const sampleWithRequiredData: IDemoMeeting = {
  id: 18640,
  date: dayjs('2023-12-04T08:25'),
  email: 'Stephen17@hotmail.com',
};

export const sampleWithPartialData: IDemoMeeting = {
  id: 18558,
  date: dayjs('2023-12-03T22:38'),
  email: 'Kobe_Roberts11@hotmail.com',
};

export const sampleWithFullData: IDemoMeeting = {
  id: 32174,
  created: dayjs('2023-12-04T05:17'),
  date: dayjs('2023-12-04T03:36'),
  email: 'Meggie_Gislason@gmail.com',
};

export const sampleWithNewData: NewDemoMeeting = {
  date: dayjs('2023-12-03T19:28'),
  email: 'Florencio_OHara@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
