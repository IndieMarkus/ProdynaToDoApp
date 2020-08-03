import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ProdynaToDoAppTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { ToDoEntryDeleteDialogComponent } from 'app/entities/to-do-entry/to-do-entry-delete-dialog.component';
import { ToDoEntryService } from 'app/entities/to-do-entry/to-do-entry.service';

describe('Component Tests', () => {
  describe('ToDoEntry Management Delete Component', () => {
    let comp: ToDoEntryDeleteDialogComponent;
    let fixture: ComponentFixture<ToDoEntryDeleteDialogComponent>;
    let service: ToDoEntryService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProdynaToDoAppTestModule],
        declarations: [ToDoEntryDeleteDialogComponent],
      })
        .overrideTemplate(ToDoEntryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ToDoEntryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoEntryService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
