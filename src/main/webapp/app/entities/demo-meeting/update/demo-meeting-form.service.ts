import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDemoMeeting, NewDemoMeeting } from '../demo-meeting.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDemoMeeting for edit and NewDemoMeetingFormGroupInput for create.
 */
type DemoMeetingFormGroupInput = IDemoMeeting | PartialWithRequiredKeyOf<NewDemoMeeting>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDemoMeeting | NewDemoMeeting> = Omit<T, 'created' | 'date'> & {
  created?: string | null;
  date?: string | null;
};

type DemoMeetingFormRawValue = FormValueOf<IDemoMeeting>;

type NewDemoMeetingFormRawValue = FormValueOf<NewDemoMeeting>;

type DemoMeetingFormDefaults = Pick<NewDemoMeeting, 'id' | 'created' | 'date'>;

type DemoMeetingFormGroupContent = {
  id: FormControl<DemoMeetingFormRawValue['id'] | NewDemoMeeting['id']>;
  created: FormControl<DemoMeetingFormRawValue['created']>;
  date: FormControl<DemoMeetingFormRawValue['date']>;
  email: FormControl<DemoMeetingFormRawValue['email']>;
};

export type DemoMeetingFormGroup = FormGroup<DemoMeetingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DemoMeetingFormService {
  createDemoMeetingFormGroup(demoMeeting: DemoMeetingFormGroupInput = { id: null }): DemoMeetingFormGroup {
    const demoMeetingRawValue = this.convertDemoMeetingToDemoMeetingRawValue({
      ...this.getFormDefaults(),
      ...demoMeeting,
    });
    return new FormGroup<DemoMeetingFormGroupContent>({
      id: new FormControl(
        { value: demoMeetingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      created: new FormControl(demoMeetingRawValue.created),
      date: new FormControl(demoMeetingRawValue.date, {
        validators: [Validators.required],
      }),
      email: new FormControl(demoMeetingRawValue.email, {
        validators: [Validators.required],
      }),
    });
  }

  getDemoMeeting(form: DemoMeetingFormGroup): IDemoMeeting | NewDemoMeeting {
    return this.convertDemoMeetingRawValueToDemoMeeting(form.getRawValue() as DemoMeetingFormRawValue | NewDemoMeetingFormRawValue);
  }

  resetForm(form: DemoMeetingFormGroup, demoMeeting: DemoMeetingFormGroupInput): void {
    const demoMeetingRawValue = this.convertDemoMeetingToDemoMeetingRawValue({ ...this.getFormDefaults(), ...demoMeeting });
    form.reset(
      {
        ...demoMeetingRawValue,
        id: { value: demoMeetingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DemoMeetingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      date: currentTime,
    };
  }

  private convertDemoMeetingRawValueToDemoMeeting(
    rawDemoMeeting: DemoMeetingFormRawValue | NewDemoMeetingFormRawValue,
  ): IDemoMeeting | NewDemoMeeting {
    return {
      ...rawDemoMeeting,
      created: dayjs(rawDemoMeeting.created, DATE_TIME_FORMAT),
      date: dayjs(rawDemoMeeting.date, DATE_TIME_FORMAT),
    };
  }

  private convertDemoMeetingToDemoMeetingRawValue(
    demoMeeting: IDemoMeeting | (Partial<NewDemoMeeting> & DemoMeetingFormDefaults),
  ): DemoMeetingFormRawValue | PartialWithRequiredKeyOf<NewDemoMeetingFormRawValue> {
    return {
      ...demoMeeting,
      created: demoMeeting.created ? demoMeeting.created.format(DATE_TIME_FORMAT) : undefined,
      date: demoMeeting.date ? demoMeeting.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
