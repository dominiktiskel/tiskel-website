import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISalesLead, NewSalesLead } from '../sales-lead.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalesLead for edit and NewSalesLeadFormGroupInput for create.
 */
type SalesLeadFormGroupInput = ISalesLead | PartialWithRequiredKeyOf<NewSalesLead>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISalesLead | NewSalesLead> = Omit<T, 'created'> & {
  created?: string | null;
};

type SalesLeadFormRawValue = FormValueOf<ISalesLead>;

type NewSalesLeadFormRawValue = FormValueOf<NewSalesLead>;

type SalesLeadFormDefaults = Pick<NewSalesLead, 'id' | 'created'>;

type SalesLeadFormGroupContent = {
  id: FormControl<SalesLeadFormRawValue['id'] | NewSalesLead['id']>;
  created: FormControl<SalesLeadFormRawValue['created']>;
  phoneNumber: FormControl<SalesLeadFormRawValue['phoneNumber']>;
  email: FormControl<SalesLeadFormRawValue['email']>;
  note: FormControl<SalesLeadFormRawValue['note']>;
  status: FormControl<SalesLeadFormRawValue['status']>;
};

export type SalesLeadFormGroup = FormGroup<SalesLeadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalesLeadFormService {
  createSalesLeadFormGroup(salesLead: SalesLeadFormGroupInput = { id: null }): SalesLeadFormGroup {
    const salesLeadRawValue = this.convertSalesLeadToSalesLeadRawValue({
      ...this.getFormDefaults(),
      ...salesLead,
    });
    return new FormGroup<SalesLeadFormGroupContent>({
      id: new FormControl(
        { value: salesLeadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      created: new FormControl(salesLeadRawValue.created),
      phoneNumber: new FormControl(salesLeadRawValue.phoneNumber),
      email: new FormControl(salesLeadRawValue.email),
      note: new FormControl(salesLeadRawValue.note),
      status: new FormControl(salesLeadRawValue.status),
    });
  }

  getSalesLead(form: SalesLeadFormGroup): ISalesLead | NewSalesLead {
    return this.convertSalesLeadRawValueToSalesLead(form.getRawValue() as SalesLeadFormRawValue | NewSalesLeadFormRawValue);
  }

  resetForm(form: SalesLeadFormGroup, salesLead: SalesLeadFormGroupInput): void {
    const salesLeadRawValue = this.convertSalesLeadToSalesLeadRawValue({ ...this.getFormDefaults(), ...salesLead });
    form.reset(
      {
        ...salesLeadRawValue,
        id: { value: salesLeadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SalesLeadFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
    };
  }

  private convertSalesLeadRawValueToSalesLead(rawSalesLead: SalesLeadFormRawValue | NewSalesLeadFormRawValue): ISalesLead | NewSalesLead {
    return {
      ...rawSalesLead,
      created: dayjs(rawSalesLead.created, DATE_TIME_FORMAT),
    };
  }

  private convertSalesLeadToSalesLeadRawValue(
    salesLead: ISalesLead | (Partial<NewSalesLead> & SalesLeadFormDefaults),
  ): SalesLeadFormRawValue | PartialWithRequiredKeyOf<NewSalesLeadFormRawValue> {
    return {
      ...salesLead,
      created: salesLead.created ? salesLead.created.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
