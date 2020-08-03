import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ProdynaToDoAppTestModule } from '../../../test.module';
import { ToDoEntryUpdateComponent } from 'app/entities/to-do-entry/to-do-entry-update.component';
import { ToDoEntryService } from 'app/entities/to-do-entry/to-do-entry.service';
import { ToDoEntry } from 'app/shared/model/to-do-entry.model';

describe('Component Tests', () => {
  describe('ToDoEntry Management Update Component', () => {
    let comp: ToDoEntryUpdateComponent;
    let fixture: ComponentFixture<ToDoEntryUpdateComponent>;
    let service: ToDoEntryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProdynaToDoAppTestModule],
        declarations: [ToDoEntryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ToDoEntryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ToDoEntryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoEntryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ToDoEntry(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ToDoEntry();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
