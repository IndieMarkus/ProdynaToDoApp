import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IToDoEntry, ToDoEntry } from 'app/shared/model/to-do-entry.model';
import { ToDoEntryService } from './to-do-entry.service';
import { ToDoEntryComponent } from './to-do-entry.component';
import { ToDoEntryDetailComponent } from './to-do-entry-detail.component';
import { ToDoEntryUpdateComponent } from './to-do-entry-update.component';

@Injectable({ providedIn: 'root' })
export class ToDoEntryResolve implements Resolve<IToDoEntry> {
  constructor(private service: ToDoEntryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IToDoEntry> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((toDoEntry: HttpResponse<ToDoEntry>) => {
          if (toDoEntry.body) {
            return of(toDoEntry.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ToDoEntry());
  }
}

export const toDoEntryRoute: Routes = [
  {
    path: '',
    component: ToDoEntryComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'ToDoEntries',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ToDoEntryDetailComponent,
    resolve: {
      toDoEntry: ToDoEntryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ToDoEntries',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ToDoEntryUpdateComponent,
    resolve: {
      toDoEntry: ToDoEntryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ToDoEntries',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ToDoEntryUpdateComponent,
    resolve: {
      toDoEntry: ToDoEntryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ToDoEntries',
    },
    canActivate: [UserRouteAccessService],
  },
];
