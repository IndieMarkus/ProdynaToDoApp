import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ToDoEntryService } from 'app/entities/to-do-entry/to-do-entry.service';
import { IToDoEntry, ToDoEntry } from 'app/shared/model/to-do-entry.model';

describe('Service Tests', () => {
  describe('ToDoEntry Service', () => {
    let injector: TestBed;
    let service: ToDoEntryService;
    let httpMock: HttpTestingController;
    let elemDefault: IToDoEntry;
    let expectedResult: IToDoEntry | IToDoEntry[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ToDoEntryService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ToDoEntry(0, 'AAAAAAA', 'AAAAAAA', false, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dueDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ToDoEntry', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dueDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ToDoEntry()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ToDoEntry', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            published: true,
            dueDate: currentDate.format(DATE_TIME_FORMAT),
            done: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ToDoEntry', () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            published: true,
            dueDate: currentDate.format(DATE_TIME_FORMAT),
            done: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ToDoEntry', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
