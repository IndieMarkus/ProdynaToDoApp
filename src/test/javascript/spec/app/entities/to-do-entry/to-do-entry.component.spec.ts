import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { ProdynaToDoAppTestModule } from '../../../test.module';
import { ToDoEntryComponent } from 'app/entities/to-do-entry/to-do-entry.component';
import { ToDoEntryService } from 'app/entities/to-do-entry/to-do-entry.service';
import { ToDoEntry } from 'app/shared/model/to-do-entry.model';

describe('Component Tests', () => {
  describe('ToDoEntry Management Component', () => {
    let comp: ToDoEntryComponent;
    let fixture: ComponentFixture<ToDoEntryComponent>;
    let service: ToDoEntryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProdynaToDoAppTestModule],
        declarations: [ToDoEntryComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(ToDoEntryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ToDoEntryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoEntryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ToDoEntry(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.toDoEntries && comp.toDoEntries[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ToDoEntry(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.toDoEntries && comp.toDoEntries[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
