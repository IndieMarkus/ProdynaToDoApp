import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IToDoEntry } from 'app/shared/model/to-do-entry.model';
import { ToDoEntryService } from './to-do-entry.service';

@Component({
  templateUrl: './to-do-entry-delete-dialog.component.html',
})
export class ToDoEntryDeleteDialogComponent {
  toDoEntry?: IToDoEntry;

  constructor(protected toDoEntryService: ToDoEntryService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.toDoEntryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('toDoEntryListModification');
      this.activeModal.close();
    });
  }
}
