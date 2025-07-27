import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { ReactiveFormsModule,FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';


import { LoginComponent } from './login.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockAuthService = {
    login: jest.fn()
  };
  const mockSessionService = {
    logIn: jest.fn()
  };
  const mockRouter = {
    navigate: jest.fn()
  };

 beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        BrowserAnimationsModule
      ],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authService.login and navigate on successful submit', () => {
    const mockResponse = { token: 'abc123', userId: 1 };
    mockAuthService.login.mockReturnValue(of(mockResponse));

    component.form.setValue({ email: 'test@example.com', password: '1234' });
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: '1234'
    });
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockResponse);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true on login error', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Login failed')));

    component.form.setValue({ email: 'test@example.com', password: '1234' });
    component.submit();

    expect(component.onError).toBe(true);
  });
});

describe('LoginComponent - Integration Test', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule, // Important pour mocker HTTPClient
        RouterTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        BrowserAnimationsModule
      ],
      providers: [
        FormBuilder
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);

    jest.spyOn(router, 'navigate'); // espionner la navigation
    fixture.detectChanges();
  });

  it('should send HTTP POST and navigate on success', fakeAsync(() => {
    component.form.setValue({ email: 'test@example.com', password: '1234' });

    component.submit();

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');

    const mockResponse = { token: 'abc123', userId: 1 };
    req.flush(mockResponse);

    tick();
    fixture.detectChanges();

    expect(component.onError).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  }));

  it('should set onError to true on HTTP error', fakeAsync(() => {
    component.form.setValue({ email: 'test@example.com', password: '1234' });

    component.submit();

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');

    req.flush('Error', { status: 401, statusText: 'Unauthorized' });

    tick();
    fixture.detectChanges();

    expect(component.onError).toBe(true);
  }));

  afterEach(() => {
    httpMock.verify(); // Vérifie qu’aucune requête HTTP non consommée n’existe
  });
});