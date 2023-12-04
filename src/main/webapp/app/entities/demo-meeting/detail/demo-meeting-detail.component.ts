import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDemoMeeting } from '../demo-meeting.model';

@Component({
  standalone: true,
  selector: 'jhi-demo-meeting-detail',
  templateUrl: './demo-meeting-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DemoMeetingDetailComponent {
  @Input() demoMeeting: IDemoMeeting | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
