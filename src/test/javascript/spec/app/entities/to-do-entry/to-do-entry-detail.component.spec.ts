import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProdynaToDoAppTestModule } from '../../../test.module';
import { ToDoEntryDetailComponent } from 'app/entities/to-do-entry/to-do-entry-detail.component';
import { ToDoEntry } from 'app/shared/model/to-do-entry.model';

describe('Component Tests', () => {
  describe('ToDoEntry Management Detail Component', () => {
    let comp: ToDoEntryDetailComponent;
    let fixture: ComponentFixture<ToDoEntryDetailComponent>;
    const route = ({ data: of({ toDoEntry: new ToDoEntry(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProdynaToDoAppTestModule],
        declarations: [ToDoEntryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ToDoEntryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ToDoEntryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load toDoEntry on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.toDoEntry).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
