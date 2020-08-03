import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProdynaToDoAppSharedModule } from 'app/shared/shared.module';
import { ToDoEntryComponent } from './to-do-entry.component';
import { ToDoEntryDetailComponent } from './to-do-entry-detail.component';
import { ToDoEntryUpdateComponent } from './to-do-entry-update.component';
import { ToDoEntryDeleteDialogComponent } from './to-do-entry-delete-dialog.component';
import { toDoEntryRoute } from './to-do-entry.route';

@NgModule({
  imports: [ProdynaToDoAppSharedModule, RouterModule.forChild(toDoEntryRoute)],
  declarations: [ToDoEntryComponent, ToDoEntryDetailComponent, ToDoEntryUpdateComponent, ToDoEntryDeleteDialogComponent],
  entryComponents: [ToDoEntryDeleteDialogComponent],
})
export class ProdynaToDoAppToDoEntryModule {}
