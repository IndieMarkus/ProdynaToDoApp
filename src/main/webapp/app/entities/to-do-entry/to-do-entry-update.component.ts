import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IToDoEntry, ToDoEntry } from 'app/shared/model/to-do-entry.model';
import { ToDoEntryService } from './to-do-entry.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-to-do-entry-update',
  templateUrl: './to-do-entry-update.component.html',
})
export class ToDoEntryUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    published: [],
    dueDate: [],
    done: [],
    creatorId: [],
  });

  constructor(
    protected toDoEntryService: ToDoEntryService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toDoEntry }) => {
      if (!toDoEntry.id) {
        const today = moment().startOf('day');
        toDoEntry.dueDate = today;
      }

      this.updateForm(toDoEntry);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(toDoEntry: IToDoEntry): void {
    this.editForm.patchValue({
      id: toDoEntry.id,
      title: toDoEntry.title,
      description: toDoEntry.description,
      published: toDoEntry.published,
      dueDate: toDoEntry.dueDate ? toDoEntry.dueDate.format(DATE_TIME_FORMAT) : null,
      done: toDoEntry.done,
      creatorId: toDoEntry.creatorId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const toDoEntry = this.createFromForm();
    if (toDoEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.toDoEntryService.update(toDoEntry));
    } else {
      this.subscribeToSaveResponse(this.toDoEntryService.create(toDoEntry));
    }
  }

  private createFromForm(): IToDoEntry {
    return {
      ...new ToDoEntry(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      published: this.editForm.get(['published'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value ? moment(this.editForm.get(['dueDate'])!.value, DATE_TIME_FORMAT) : undefined,
      done: this.editForm.get(['done'])!.value,
      creatorId: this.editForm.get(['creatorId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IToDoEntry>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
