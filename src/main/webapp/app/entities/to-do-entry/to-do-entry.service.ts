import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IToDoEntry } from 'app/shared/model/to-do-entry.model';

type EntityResponseType = HttpResponse<IToDoEntry>;
type EntityArrayResponseType = HttpResponse<IToDoEntry[]>;

@Injectable({ providedIn: 'root' })
export class ToDoEntryService {
  public resourceUrl = SERVER_API_URL + 'api/to-do-entries';

  constructor(protected http: HttpClient) {}

  create(toDoEntry: IToDoEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoEntry);
    return this.http
      .post<IToDoEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(toDoEntry: IToDoEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoEntry);
    return this.http
      .put<IToDoEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IToDoEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IToDoEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(toDoEntry: IToDoEntry): IToDoEntry {
    const copy: IToDoEntry = Object.assign({}, toDoEntry, {
      dueDate: toDoEntry.dueDate && toDoEntry.dueDate.isValid() ? toDoEntry.dueDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dueDate = res.body.dueDate ? moment(res.body.dueDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((toDoEntry: IToDoEntry) => {
        toDoEntry.dueDate = toDoEntry.dueDate ? moment(toDoEntry.dueDate) : undefined;
      });
    }
    return res;
  }
}
