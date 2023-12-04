import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDemoMeeting } from '../demo-meeting.model';
import { DemoMeetingService } from '../service/demo-meeting.service';
import { DemoMeetingFormService, DemoMeetingFormGroup } from './demo-meeting-form.service';

@Component({
  standalone: true,
  selector: 'jhi-demo-meeting-update',
  templateUrl: './demo-meeting-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DemoMeetingUpdateComponent implements OnInit {
  isSaving = false;
  demoMeeting: IDemoMeeting | null = null;

  editForm: DemoMeetingFormGroup = this.demoMeetingFormService.createDemoMeetingFormGroup();

  constructor(
    protected demoMeetingService: DemoMeetingService,
    protected demoMeetingFormService: DemoMeetingFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demoMeeting }) => {
      this.demoMeeting = demoMeeting;
      if (demoMeeting) {
        this.updateForm(demoMeeting);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demoMeeting = this.demoMeetingFormService.getDemoMeeting(this.editForm);
    if (demoMeeting.id !== null) {
      this.subscribeToSaveResponse(this.demoMeetingService.update(demoMeeting));
    } else {
      this.subscribeToSaveResponse(this.demoMeetingService.create(demoMeeting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemoMeeting>>): void {
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

  protected updateForm(demoMeeting: IDemoMeeting): void {
    this.demoMeeting = demoMeeting;
    this.demoMeetingFormService.resetForm(this.editForm, demoMeeting);
  }
}
