import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

const dummySessions: Session[] = [
  {
    id: 1,
    name: 'Session 1',
    description: 'Desc 1',
    date: new Date('2025-07-14T00:00:00Z'),
    teacher_id: 1,
    users: [1, 2]
  },
  {
    id: 2,
    name: 'Session 2',
    description: 'Desc 2',
    date: new Date('2025-08-15T00:00:00Z'),
    teacher_id: 2,
    users: [3]
  }
];

  const dummySession: Session = {
  id: 1,
  name: 'Session 1',
  description: 'Description 1',
  date: new Date('2025-07-14T00:00:00Z'),
  teacher_id: 1,
  users: [1, 2],
  createdAt: new Date(),
  updatedAt: new Date(),
};

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions via GET', () => {
    service.all().subscribe(sessions => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(dummySessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(dummySessions);
  });

  it('should retrieve one session by id via GET', () => {
    service.detail('1').subscribe(session => {
      expect(session).toEqual(dummySession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(dummySession);
  });

  it('should delete a session by id via DELETE', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({ success: true });
  });

  it('should create a session via POST', () => {
    service.create(dummySession).subscribe(session => {
      expect(session).toEqual(dummySession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dummySession);
    req.flush(dummySession);
  });

  it('should update a session via PUT', () => {
    service.update('1', dummySession).subscribe(session => {
      expect(session).toEqual(dummySession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(dummySession);
    req.flush(dummySession);
  });

  it('should call participate endpoint via POST', () => {
    service.participate('1', '10').subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('api/session/1/participate/10');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should call unParticipate endpoint via DELETE', () => {
    service.unParticipate('1', '10').subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('api/session/1/participate/10');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

});
