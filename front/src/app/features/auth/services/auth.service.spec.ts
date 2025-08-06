import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should register a user', () => {
    const dummyRegisterRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '123456'
    };

    service.register(dummyRegisterRequest).subscribe(response => {
      expect(response).toBeUndefined(); 
    });

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dummyRegisterRequest);

    req.flush(null); 
  });

  it('should login a user', () => {
    const dummyLoginRequest: LoginRequest = {
      email: 'test@example.com',
      password: '123456'
    };

    const dummySessionInfo: SessionInformation = {
        token: 'token123',
        type: 'user',
        id: 1,
        username: 'john.doe',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
    };

    service.login(dummyLoginRequest).subscribe(response => {
      expect(response).toEqual(dummySessionInfo);
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dummyLoginRequest);

    req.flush(dummySessionInfo);
  });
});

describe('AuthService - Integration Tests', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should register a user successfully', () => {
    const dummyRegisterRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '123456'
    };

    service.register(dummyRegisterRequest).subscribe({
      next: (response) => {
        expect(response).toBeUndefined(); 
      },
      error: () => {
        
        fail('La requête register ne devait pas échouer');
      }
    });

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dummyRegisterRequest);

    req.flush(null);
  });

  it('should handle HTTP error on register', () => {
    const dummyRegisterRequest: RegisterRequest = {
      email: 'error@example.com',
      firstName: 'Error',
      lastName: 'Test',
      password: '000000'
    };

    service.register(dummyRegisterRequest).subscribe({
      next: () => {
        fail('La requête register devait échouer');
      },
      error: (error) => {
       
        expect(error.status).toBe(400);
      }
    });

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');

    req.flush(
      { message: 'Bad request' },
      { status: 400, statusText: 'Bad Request' }
    );
  });
});