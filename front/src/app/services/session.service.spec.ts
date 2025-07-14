import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { take } from 'rxjs/operators';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  const mockSessionInfo: SessionInformation = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'user1',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should emit false initially on $isLogged()', (done) => {
    service.$isLogged().pipe(take(1)).subscribe(value => {
      expect(value).toBe(false);
      done();
    });
  });

  it('should log in user and emit true', (done) => {
    service.logIn(mockSessionInfo);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInfo);

    service.$isLogged().pipe(take(1)).subscribe(value => {
      expect(value).toBe(true);
      done();
    });
  });

  it('should log out user and emit false', (done) => {
    service.logIn(mockSessionInfo); // d'abord connecté
    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();

    service.$isLogged().pipe(take(1)).subscribe(value => {
      expect(value).toBe(false);
      done();
    });
  });
});