import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { User } from '../interfaces/user.interface';
import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const dummyUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    admin: false,
    password: 'hashedpassword',
    createdAt: new Date('2025-07-14T00:00:00Z'),
    updatedAt: new Date('2025-07-15T00:00:00Z')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id via GET', () => {
    service.getById('1').subscribe(user => {
      expect(user).toEqual(dummyUser);
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(dummyUser);
  });

  it('should delete user by id via DELETE', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({ success: true });
  });
});

describe('UserService Integration', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const dummyUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    admin: false,
    password: 'hashedpassword',
    createdAt: new Date('2025-07-14T00:00:00Z'),
    updatedAt: new Date('2025-07-15T00:00:00Z')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], 
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController); 
  });

  afterEach(() => {
    httpMock.verify(); 
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id from fake backend', () => {
    service.getById('1').subscribe((user: User) => {
      expect(user).toEqual(dummyUser);
    });

    const req = httpMock.expectOne('api/user/1'); 
    expect(req.request.method).toBe('GET');
    req.flush(dummyUser); 
  });

  it('should delete user by id from fake backend', () => {
    service.delete('1').subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({ success: true });
  });
});