import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SalesLeadStatus } from 'app/entities/enumerations/sales-lead-status.model';
import { ISalesLead } from '../sales-lead.model';
import { SalesLeadService } from '../service/sales-lead.service';
import { SalesLeadFormService, SalesLeadFormGroup } from './sales-lead-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sales-lead-update',
  templateUrl: './sales-lead-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SalesLeadUpdateComponent implements OnInit {
  isSaving = false;
  salesLead: ISalesLead | null = null;
  salesLeadStatusValues = Object.keys(SalesLeadStatus);

  editForm: SalesLeadFormGroup = this.salesLeadFormService.createSalesLeadFormGroup();

  constructor(
    protected salesLeadService: SalesLeadService,
    protected salesLeadFormService: SalesLeadFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salesLead }) => {
      this.salesLead = salesLead;
      if (salesLead) {
        this.updateForm(salesLead);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salesLead = this.salesLeadFormService.getSalesLead(this.editForm);
    if (salesLead.id !== null) {
      this.subscribeToSaveResponse(this.salesLeadService.update(salesLead));
    } else {
      this.subscribeToSaveResponse(this.salesLeadService.create(salesLead));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalesLead>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(salesLead: ISalesLead): void {
    this.salesLead = salesLead;
    this.salesLeadFormService.resetForm(this.editForm, salesLead);
  }
}
