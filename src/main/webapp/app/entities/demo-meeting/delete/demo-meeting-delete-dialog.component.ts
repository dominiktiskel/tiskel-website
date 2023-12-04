import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDemoMeeting } from '../demo-meeting.model';
import { DemoMeetingService } from '../service/demo-meeting.service';

@Component({
  standalone: true,
  templateUrl: './demo-meeting-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DemoMeetingDeleteDialogComponent {
  demoMeeting?: IDemoMeeting;

  constructor(
    protected demoMeetingService: DemoMeetingService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demoMeetingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
