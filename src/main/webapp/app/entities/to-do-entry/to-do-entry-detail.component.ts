import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IToDoEntry } from 'app/shared/model/to-do-entry.model';

@Component({
  selector: 'jhi-to-do-entry-detail',
  templateUrl: './to-do-entry-detail.component.html',
})
export class ToDoEntryDetailComponent implements OnInit {
  toDoEntry: IToDoEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toDoEntry }) => (this.toDoEntry = toDoEntry));
  }

  previousState(): void {
    window.history.back();
  }
}
